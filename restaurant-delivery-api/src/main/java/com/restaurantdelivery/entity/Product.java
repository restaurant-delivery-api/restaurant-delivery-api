package com.restaurantdelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Table(name = "products")
@Getter
@Setter
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private Integer price;

    private Integer oldPrice;

    @OneToOne(fetch = FetchType.EAGER)
    private Category category;

    @OneToMany(fetch = FetchType.EAGER)
    private List<FilledProperty> filledProperties;

    @OneToMany(fetch = FetchType.EAGER)
    private List<ExtraProperty> extraPropertyList;

    @Transient
    private Long categoryId;

    @Column(length = 500)
    private String info;

    private List<String> photoList;

    public Product() {
        this.filledProperties = new ArrayList<>();
    }

    public void addFilledProperty(FilledProperty filledProperty) {
        filledProperties.add(filledProperty);
    }

    public void addExtraProperty(ExtraProperty extraProperty) {extraPropertyList.add(extraProperty); }
}