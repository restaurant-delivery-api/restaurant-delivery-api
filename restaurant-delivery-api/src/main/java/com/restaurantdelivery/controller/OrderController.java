package com.restaurantdelivery.controller;

import com.restaurantdelivery.dto.OrderDto;
import com.restaurantdelivery.entity.Order;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.service.OrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
@RequestMapping("/api/v1/order")
public class OrderController {

    private OrderService orderService;
    private ModelMapper orderMapper;

    @GetMapping("/all")
    @ResponseBody
    public List<OrderDto> getOrders() {
        return orderService.getAllOrders().stream().map(this::convertToDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public OrderDto addOrder(@RequestBody OrderDto orderDto) {
        return convertToDto(orderService.addOrder(convertToEntity(orderDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public OrderDto updateOrder(@PathVariable("id") Long id,
                                                @RequestBody OrderDto orderDto) {
        if (!Objects.equals(id, orderDto.getId()))
        {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(orderService.updateOrder(id, convertToEntity(orderDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable("id") Long id) {
        orderService.deleteOrderById(id);
    }

    private OrderDto convertToDto(Order order) {
        return orderMapper.map(order, OrderDto.class);
    }

    private Order convertToEntity(OrderDto orderDto) {
        return orderMapper.map(orderDto, Order.class);
    }

}