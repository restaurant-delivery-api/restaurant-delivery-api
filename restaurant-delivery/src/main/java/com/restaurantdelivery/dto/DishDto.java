package com.restaurantdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DishDto {
    private Long id;
    private String name;
    private String description;
}
