package ru.poolnsk.pool.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import ru.poolnsk.pool.dto.CatalogDto;
import ru.poolnsk.pool.entity.Cart;
import ru.poolnsk.pool.entity.Category;
import ru.poolnsk.pool.entity.Product;
import ru.poolnsk.pool.entity.Section;
import ru.poolnsk.pool.repository.CartRepository;
import ru.poolnsk.pool.service.CartService;
import ru.poolnsk.pool.service.ProductService;
import ru.poolnsk.pool.service.SectionService;
import ru.poolnsk.pool.service.UserService;
import ru.poolnsk.pool.utils.Catalog;

import java.util.*;

@Controller
@ResponseStatus(HttpStatus.OK)
@RequiredArgsConstructor
public class CartController {
    private final ProductService productService;
    private final CartService cartService;
    private final SectionService sectionService;

    @PostMapping("/addToCart/{productId}-{num}")
    public void addToCart(@PathVariable("productId") String productId, @PathVariable("num") String num, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        for (int i = 0; i<Long.parseLong(num); i++) {
            cart.addProduct(product);
        }
        cartService.save(cart);
    }

    @PostMapping("/deleteFromCart/{productId}")
    public void deleteFromCart(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        while (cart.getProducts().contains(product)) {
            cart.deleteProduct(product);
        }
        cartService.save(cart);
    }

    @PostMapping("/decreaseAmount/{productId}")
    public void decreaseAmount(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        cart.deleteProduct(product);
        cartService.save(cart);
    }

    @PostMapping("/increaseAmount/{productId}")
    public void increaseAmount(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response) {
        Cart cart = cartService.getOrCreateCart(request, response);
        Product product = productService.getById(Long.parseLong(productId));
        cart.addProduct(product);
        cartService.save(cart);
    }

    @GetMapping("/cart")
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