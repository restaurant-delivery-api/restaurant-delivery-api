package com.restaurantdelivery.response;

import lombok.Data;

import java.util.Date;

@Data
public class AppResponse {
    private String message;
    private Object payload;
    private Date timestamp;

    public AppResponse(String message, Object payload) {
        this.message = message;
        this.payload = payload;
        this.timestamp = new Date();
    }

    public AppResponse(String message) {
        this.message = message;
        this.payload = null;
        this.timestamp = new Date();
    }
}
