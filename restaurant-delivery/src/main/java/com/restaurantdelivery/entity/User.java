package com.restaurantdelivery.entity;

import com.restaurantdelivery.entity.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data

@Entity
@Table(name = "usr")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String secondName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column
    private String email;

    @OneToMany
    @JoinColumn(name = "delivery_point_id")
    private List<DeliveryPoint> deliveryPoints;

    @Column(nullable = false)
    private UserRole userRole;
}
