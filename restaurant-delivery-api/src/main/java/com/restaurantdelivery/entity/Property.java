package com.restaurantdelivery.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name="category_property")
public class Property {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    private ValueTypes valueType;

    @Transient
    private Long categoryId;

    private Boolean top;

    @Transient
    private String value;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Category> categories;

    public Property() {
        this.categories = new ArrayList<Category>();
    }

    public void addCategory(Category category) {this.categories.add(category);}

    public void removeCategory(Category category) {
        this.categories.remove(category);
    }
}