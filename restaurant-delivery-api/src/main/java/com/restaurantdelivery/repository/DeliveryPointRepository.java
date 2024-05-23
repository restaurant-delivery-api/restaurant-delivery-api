package com.restaurantdelivery.repository;

import com.restaurantdelivery.entity.DeliveryPoint;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryPointRepository extends JpaRepository<DeliveryPoint, Long> {
}
