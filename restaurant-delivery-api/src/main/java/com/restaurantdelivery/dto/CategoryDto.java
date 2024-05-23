package com.restaurantdelivery.dto;

import com.restaurantdelivery.entity.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class CategoryDto {
    private Long id;
    private String name;
    private List<Product> products;
}
