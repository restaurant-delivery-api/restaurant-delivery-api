package com.restaurantdelivery.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.restaurantdelivery.entity.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
