package ru.poolnsk.pool.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.poolnsk.pool.entity.Role;


@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
}
