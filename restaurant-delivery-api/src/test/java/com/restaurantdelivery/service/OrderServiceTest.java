package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.Order;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.OrderRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllOrders() {
        List<Order> orders = new ArrayList<>();
        orders.add(new Order());
        orders.add(new Order());

        when(orderRepository.findAll()).thenReturn(orders);

        List<Order> result = orderService.getAllOrders();

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findAll();
    }

    @Test
    void testGetOrderByIdOrThrow() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        Order result = orderService.getOrderByIdOrThrow(1L);

        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testGetOrderByIdOrThrow_NotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        ServerException exception = assertThrows(ServerException.class, () -> {
            orderService.getOrderByIdOrThrow(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("Order with id 1 does not exist", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> orderService.nonExistOrThrow(order));
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow_AlreadyExists() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        ServerException exception = assertThrows(ServerException.class, () -> {
            orderService.nonExistOrThrow(order);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("Order with id 1 already exists", exception.getMessage());
        verify(orderRepository, times(1)).findById(1L);
    }

    @Test
    void testAddOrder() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.findById(1L)).thenReturn(Optional.empty());
        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.addOrder(order);

        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(1L);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testUpdateOrder() {
        Order order = new Order();
        order.setId(1L);

        when(orderRepository.save(order)).thenReturn(order);

        Order result = orderService.updateOrder(1L, order);

        assertEquals(order, result);
        verify(orderRepository, times(1)).save(order);
    }

    @Test
    void testDeleteOrderById() {
        doNothing().when(orderRepository).deleteById(1L);

        orderService.deleteOrderById(1L);

        verify(orderRepository, times(1)).deleteById(1L);
    }
}
