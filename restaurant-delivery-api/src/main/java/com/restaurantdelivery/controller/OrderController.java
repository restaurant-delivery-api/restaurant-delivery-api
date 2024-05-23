package com.restaurantdelivery.controller;

import com.restaurantdelivery.api.OrderApi;
import com.restaurantdelivery.dto.OrderDto;
import com.restaurantdelivery.entity.Order;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.service.OrderService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
public class OrderController implements OrderApi {

    private OrderService orderService;
    private ModelMapper orderMapper;


    public List<OrderDto> getOrders() {
        return orderService.getAllOrders().stream().map(this::convertToDto).toList();
    }


    public OrderDto addOrder(OrderDto orderDto) {
        return convertToDto(orderService.addOrder(convertToEntity(orderDto)));
    }


    public OrderDto updateOrder(Long id, OrderDto orderDto) {
        if (!Objects.equals(id, orderDto.getId()))
        {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(orderService.updateOrder(id, convertToEntity(orderDto)));
    }


    public void deleteMenu(Long id) {
        orderService.deleteOrderById(id);
    }

    private OrderDto convertToDto(Order order) {
        return orderMapper.map(order, OrderDto.class);
    }

    private Order convertToEntity(OrderDto orderDto) {
        return orderMapper.map(orderDto, Order.class);
    }

}