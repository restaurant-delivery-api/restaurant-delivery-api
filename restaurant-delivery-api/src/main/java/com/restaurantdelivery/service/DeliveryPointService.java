package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.DeliveryPoint;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.DeliveryPointRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@AllArgsConstructor
@Service
public class DeliveryPointService {

    private DeliveryPointRepository deliverPointRepository;

    public List<DeliveryPoint> getAllDeliveryPoints() {
        return deliverPointRepository.findAll();
    }

    public DeliveryPoint getDeliveryPointByIdOrThrow(Long id) {
        return deliverPointRepository.findById(id).orElseThrow(() ->
                new ServerException(HttpStatus.NOT_FOUND,
                        "DeliveryPoint with id " + id + " does not exist")
        );
    }

    public void nonExistOrThrow(DeliveryPoint deliveryPoint) {
        deliverPointRepository.findById(deliveryPoint.getId()).ifPresent(usr -> {
            throw new ServerException(HttpStatus.BAD_REQUEST,
                    "DeliveryPoint with id " + usr.getId() + " already exists");
        });
    }

    public DeliveryPoint addDeliveryPoint(DeliveryPoint deliveryPoint) {
        nonExistOrThrow(deliveryPoint);
        return deliverPointRepository.save(deliveryPoint);
    }

    public DeliveryPoint updateDeliveryPoint(Long id, DeliveryPoint deliveryPoint) {
        return deliverPointRepository.save(deliveryPoint);
    }

    public void deleteDeliveryPointById(Long id) {
        deliverPointRepository.deleteById(id);
    }
}
