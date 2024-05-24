package com.restaurantdelivery.api;

import com.restaurantdelivery.configuration.Constants;
import com.restaurantdelivery.dto.OrderDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping(Constants.BASE_API_PATH)
public interface CartApi {
    @PostMapping("/addToCart/{productId}-{num}")
    void addToCart(@PathVariable("productId") String productId, @PathVariable("num") String num, HttpServletRequest request, HttpServletResponse response);

    @PostMapping("/deleteFromCart/{productId}")
    void deleteFromCart(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response);

    @PostMapping("/decreaseAmount/{productId}")
    void decreaseAmount(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response);

    @PostMapping("/increaseAmount/{productId}")
    void increaseAmount(@PathVariable("productId") String productId, HttpServletRequest request, HttpServletResponse response);

    @GetMapping("/cart")
    ModelAndView viewCart(HttpServletRequest request, HttpServletResponse response);

}