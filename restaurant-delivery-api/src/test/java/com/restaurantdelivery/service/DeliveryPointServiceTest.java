package com.restaurantdelivery.service;

import com.restaurantdelivery.entity.DeliveryPoint;
import com.restaurantdelivery.exception.ServerException;
import com.restaurantdelivery.repository.DeliveryPointRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeliveryPointServiceTest {

    @Mock
    private DeliveryPointRepository deliveryPointRepository;

    @InjectMocks
    private DeliveryPointService deliveryPointService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllDeliveryPoints() {
        DeliveryPoint deliveryPoint1 = new DeliveryPoint();
        DeliveryPoint deliveryPoint2 = new DeliveryPoint();
        List<DeliveryPoint> deliveryPointList = Arrays.asList(deliveryPoint1, deliveryPoint2);

        when(deliveryPointRepository.findAll()).thenReturn(deliveryPointList);

        List<DeliveryPoint> result = deliveryPointService.getAllDeliveryPoints();

        assertEquals(2, result.size());
        verify(deliveryPointRepository, times(1)).findAll();
    }

    @Test
    void testGetDeliveryPointByIdOrThrow() {
        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setId(1L);

        when(deliveryPointRepository.findById(1L)).thenReturn(Optional.of(deliveryPoint));

        DeliveryPoint result = deliveryPointService.getDeliveryPointByIdOrThrow(1L);

        assertEquals(deliveryPoint, result);
        verify(deliveryPointRepository, times(1)).findById(1L);
    }

    @Test
    void testGetDeliveryPointByIdOrThrow_NotFound() {
        when(deliveryPointRepository.findById(1L)).thenReturn(Optional.empty());

        ServerException exception = assertThrows(ServerException.class, () -> {
            deliveryPointService.getDeliveryPointByIdOrThrow(1L);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatus());
        assertEquals("DeliveryPoint with id 1 does not exist", exception.getMessage());
        verify(deliveryPointRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow() {
        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setId(1L);

        when(deliveryPointRepository.findById(1L)).thenReturn(Optional.empty());

        assertDoesNotThrow(() -> deliveryPointService.nonExistOrThrow(deliveryPoint));
        verify(deliveryPointRepository, times(1)).findById(1L);
    }

    @Test
    void testNonExistOrThrow_AlreadyExists() {
        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setId(1L);

        when(deliveryPointRepository.findById(1L)).thenReturn(Optional.of(deliveryPoint));

        ServerException exception = assertThrows(ServerException.class, () -> {
            deliveryPointService.nonExistOrThrow(deliveryPoint);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatus());
        assertEquals("DeliveryPoint with id 1 already exists", exception.getMessage());
        verify(deliveryPointRepository, times(1)).findById(1L);
    }

    @Test
    void testAddDeliveryPoint() {
        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setId(1L);

        when(deliveryPointRepository.findById(1L)).thenReturn(Optional.empty());
        when(deliveryPointRepository.save(deliveryPoint)).thenReturn(deliveryPoint);

        DeliveryPoint result = deliveryPointService.addDeliveryPoint(deliveryPoint);

        assertEquals(deliveryPoint, result);
        verify(deliveryPointRepository, times(1)).findById(1L);
        verify(deliveryPointRepository, times(1)).save(deliveryPoint);
    }

    @Test
    void testUpdateDeliveryPoint() {
        DeliveryPoint deliveryPoint = new DeliveryPoint();
        deliveryPoint.setId(1L);

        when(deliveryPointRepository.save(deliveryPoint)).thenReturn(deliveryPoint);

        DeliveryPoint result = deliveryPointService.updateDeliveryPoint(1L, deliveryPoint);

        assertEquals(deliveryPoint, result);
        verify(deliveryPointRepository, times(1)).save(deliveryPoint);
    }

    @Test
    void testDeleteDeliveryPointById() {
        doNothing().when(deliveryPointRepository).deleteById(1L);

        deliveryPointService.deleteDeliveryPointById(1L);

        verify(deliveryPointRepository, times(1)).deleteById(1L);
    }
}
