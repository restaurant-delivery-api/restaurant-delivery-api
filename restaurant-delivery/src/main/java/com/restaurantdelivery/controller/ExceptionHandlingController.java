package com.restaurantdelivery.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.restaurantdelivery.exception.ServerException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;

@ControllerAdvice
public class ExceptionHandlingController {
    @ExceptionHandler(ServerException.class)
    private void handle(HttpServletResponse response, ServerException serverException) {
        response.setStatus(serverException.getStatus().value());
        response.addHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE);
        ObjectMapper mapper = new ObjectMapper();
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(response.getOutputStream()))) {
            bw.write(mapper.writeValueAsString(serverException.getAnswer()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
