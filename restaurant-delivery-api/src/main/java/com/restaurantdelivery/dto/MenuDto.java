package com.restaurantdelivery.dto;

import com.restaurantdelivery.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class MenuDto {
    private Long id;
    private List<Category> categories;
}
