package com.restaurantdelivery.dto;

import lombok.Data;
import com.restaurantdelivery.entity.Category;
import com.restaurantdelivery.entity.Section;

import java.util.List;
import java.util.Map;

@Data
public class CatalogDto {
    Map<Section, List<Category>> catalog;
}