package com.restaurantdelivery.dto;

import com.restaurantdelivery.entity.DeliveryPoint;
import com.restaurantdelivery.entity.Product;
import com.restaurantdelivery.entity.User;
import com.restaurantdelivery.entity.enums.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class OrderDto {
    private Long id;
    private User user;
    private Date creation_timestamp;
    private OrderStatus orderStatus;
    private String comment;
    private DeliveryPoint deliveryPoint;
    private List<Product> products;
}
