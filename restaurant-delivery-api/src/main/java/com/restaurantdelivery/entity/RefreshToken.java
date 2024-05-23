package com.restaurantdelivery.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @NotBlank
    private String token;

    @NotNull
    private Date issuedDate;

    @NotNull
    private Date expirationDate;

    public RefreshToken(User user, Duration lifetime) {
        this.user = user;
        this.token = UUID.randomUUID().toString();
        this.issuedDate = new Date();
        this.expirationDate = new Date(issuedDate.getTime() + lifetime.toMillis());
    }
}
