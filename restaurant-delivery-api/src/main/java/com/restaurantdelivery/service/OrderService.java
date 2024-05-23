package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.Order;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.OrderRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class OrderService {

    private OrderRepository deliverPointRepository;

    public List<Order> getAllOrders() {
        return deliverPointRepository.findAll();
    }

    public Order getOrderByIdOrThrow(Long id) {
        return deliverPointRepository.findById(id).orElseThrow(() ->
                new ServerException(HttpStatus.NOT_FOUND,
                        "Order with id " + id + " does not exist")
        );
    }

    public void nonExistOrThrow(Order order) {
        deliverPointRepository.findById(order.getId()).ifPresent(usr -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "Order with id " + usr.getId() + " already exists");
        });
    }

    public Order addOrder(Order order) {
        nonExistOrThrow(order);
        return deliverPointRepository.save(order);
    }

    public Order updateOrder(Long id, Order order) {
        return deliverPointRepository.save(order);
    }

    public void deleteOrderById(Long id) {
        deliverPointRepository.deleteById(id);
    }
}
