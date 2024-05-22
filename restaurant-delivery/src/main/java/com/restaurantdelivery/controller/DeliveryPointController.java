package com.restaurantdelivery.controller;

import com.restaurantdelivery.dto.DeliveryPointDto;
import com.restaurantdelivery.entity.DeliveryPoint;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.service.DeliveryPointService;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;

@AllArgsConstructor

@RestController
@RequestMapping("/api/v1/delivery_point")
public class DeliveryPointController {

    private DeliveryPointService deliveryPointService;
    private ModelMapper deliveryPointMapper;

    @GetMapping("/all")
    @ResponseBody
    public List<DeliveryPointDto> getDeliveryPoints() {
        return deliveryPointService.getAllDeliveryPoints().stream().map(this::convertToDto).toList();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @ResponseBody
    public DeliveryPointDto addDeliveryPoint(@RequestBody DeliveryPointDto deliveryPointDto) {
        return convertToDto(deliveryPointService.addDeliveryPoint(convertToEntity(deliveryPointDto)));
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    @ResponseBody
    public DeliveryPointDto updateDeliveryPoint(@PathVariable("id") Long id,
                                                @RequestBody DeliveryPointDto deliveryPointDto) {
        if (!Objects.equals(id, deliveryPointDto.getId()))
        {
            throw new ServerException(HttpStatus.BAD_REQUEST, "IDs don't match");
        }
        return convertToDto(deliveryPointService.updateDeliveryPoint(id, convertToEntity(deliveryPointDto)));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteMenu(@PathVariable("id") Long id) {
        deliveryPointService.deleteDeliveryPointById(id);
    }

    private DeliveryPointDto convertToDto(DeliveryPoint deliveryPoint) {
        return deliveryPointMapper.map(deliveryPoint, DeliveryPointDto.class);
    }

    private DeliveryPoint convertToEntity(DeliveryPointDto deliveryPointDto) {
        return deliveryPointMapper.map(deliveryPointDto, DeliveryPoint.class);
    }

}
