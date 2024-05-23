package com.restaurantdelivery.controller;

import com.restaurantdelivery.api.DeliveryPointApi;
import com.restaurantdelivery.dto.DeliveryPointDto;
import com.restaurantdelivery.entity.DeliveryPoint;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.service.DeliveryPointService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
public class DeliveryPointController implements DeliveryPointApi {

    private DeliveryPointService deliveryPointService;
    private ModelMapper deliveryPointMapper;


    public List<DeliveryPointDto> getDeliveryPoints() {
        return deliveryPointService.getAllDeliveryPoints().stream().map(this::convertToDto).toList();
    }


    public DeliveryPointDto addDeliveryPoint(DeliveryPointDto deliveryPointDto) {
        return convertToDto(deliveryPointService.addDeliveryPoint(convertToEntity(deliveryPointDto)));
    }


    public DeliveryPointDto updateDeliveryPoint(Long id,
                                                DeliveryPointDto deliveryPointDto) {
        if (!Objects.equals(id, deliveryPointDto.getId())) {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(deliveryPointService.updateDeliveryPoint(id, convertToEntity(deliveryPointDto)));
    }


    public void deleteDeliveryPoint(Long id) {
        deliveryPointService.deleteDeliveryPointById(id);
    }

    private DeliveryPointDto convertToDto(DeliveryPoint deliveryPoint) {
        return deliveryPointMapper.map(deliveryPoint, DeliveryPointDto.class);
    }

    private DeliveryPoint convertToEntity(DeliveryPointDto deliveryPointDto) {
        return deliveryPointMapper.map(deliveryPointDto, DeliveryPoint.class);
    }

}
