package com.restaurantdelivery.api;

import com.restaurantdelivery.configuration.Constants;
import com.restaurantdelivery.dto.OrderDto;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.PositiveOrZero;
import java.util.List;

@RequestMapping(Constants.BASE_API_PATH + "/order")
public interface OrderApi {


    @GetMapping("/all")
    @ResponseBody
    List<OrderDto> getOrders();

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    OrderDto addOrder(@RequestBody OrderDto orderDto);


    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    OrderDto updateOrder(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id,
                         @RequestBody OrderDto orderDto);

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteMenu(@PathVariable("id") @PositiveOrZero(message = "id must be not negative") Long id);

}