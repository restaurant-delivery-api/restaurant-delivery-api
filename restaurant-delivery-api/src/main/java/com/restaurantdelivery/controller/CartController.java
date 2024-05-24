package com.restaurantdelivery.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import com.restaurantdelivery.dto.CatalogDto;
import com.restaurantdelivery.entity.Cart;
import com.restaurantdelivery.entity.Category;
import com.restaurantdelivery.entity.Product;
import com.restaurantdelivery.entity.Section;
import com.restaurantdelivery.repository.CartRepository;
import com.restaurantdelivery.service.CartService;
import com.restaurantdelivery.service.ProductService;
import com.restaurantdelivery.service.SectionService;
import com.restaurantdelivery.service.UserService;
import com.restaurantdelivery.utils.Catalog;

import java.util.*;

@Controller
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class CartController implements CartApi{
    private final ProductService productService;
    private final CartService cartService;
    private final SectionService sectionService;

    public void addToCart(String productId, String num, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        for (int i = 0; i<Long.parseLong(num); i++) {
            cart.addProduct(product);
        }
        cartService.save(cart);
    }

    public void deleteFromCart(String productId, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        while (cart.getProducts().contains(product)) {
            cart.deleteProduct(product);
        }
        cartService.save(cart);
    }

    public void decreaseAmount(String productId, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        cart.deleteProduct(product);
        cartService.save(cart);
    }

    public void increaseAmount(String productId, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        cart.addProduct(product);
        cartService.save(cart);
    }

    public ModelAndView viewCart(HttpServletRequest request, HttpServletResponse response) {
        ModelAndView modelAndView = new ModelAndView("cart");
        Cart cart = cartService.getOrCreateCart(request, response);
        List<Product> productList = cart.getProducts();
        Map<Product, Integer> productMap = new TreeMap<>(Comparator.comparingLong(Product::getId));
        Integer totalSum = 0;
        Integer totalDiscount = 0;
        for (Product product : productList) {
            if (productMap.containsKey(product)) {
                Integer amount = productMap.get(product);
                productMap.put(product, amount + 1);
            } else {
                productMap.put(product, 1);
            }
            totalSum += product.getPrice();
            if (product.getOldPrice() != null) {
                totalDiscount += product.getOldPrice() - product.getPrice();
            }
        }

        Catalog.setCatalog(modelAndView, sectionService);
        modelAndView.getModel().put("productAmount", cartService.getOrCreateCart(request, response).getProducts().size());
        modelAndView.getModel().put("totalSum", totalSum);
        modelAndView.getModel().put("totalDiscount", totalDiscount);
        modelAndView.getModel().put("amount", productList.size());
        modelAndView.getModel().put("productMap", productMap);
        return modelAndView;
    }
}