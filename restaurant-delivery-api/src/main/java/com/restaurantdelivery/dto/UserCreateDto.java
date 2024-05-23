package com.restaurantdelivery.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserCreateDto {
    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Surname is required")
    private String surname;

    @NotBlank(message = "Email is required")
    @Email
    private String email;

    @Size(min=8, message = "Password is too short min 8 chars")
    private String password;
}
