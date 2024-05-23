package com.restaurantdelivery.entity;

import com.restaurantdelivery.entity.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "ordr")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JoinColumn(name = "usr_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private Date creation_timestamp;

    @Column(nullable = false)
    private OrderStatus orderStatus;

    @Column
    private String comment;

    @OneToOne
    @JoinColumn(name = "delivery_point_id", nullable = false)
    private DeliveryPoint deliveryPoint;

//    @OneToMany
//    @JoinColumn(name = "product_id", nullable = false)
//    private List<Product> products;

}
