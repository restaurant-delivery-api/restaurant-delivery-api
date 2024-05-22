package com.restaurantdelivery.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class DeliveryPointDto {
    private Long id;
    private String street;
    private String house;
    private String flat;
}
