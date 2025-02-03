package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.Role;
import com.itelectric.backend.v1.domain.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PrivilegeRepository extends JpaRepository<Role, UUID> {
    @Query("SELECT p FROM Privilege p WHERE p.name = :name AND p.isActive = true AND p.isDeleted = false")
    Optional<User> findByName(@Param("name") String name);
}
