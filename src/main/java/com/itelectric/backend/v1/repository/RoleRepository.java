package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    @Query("SELECT r FROM Role r WHERE r.name = :name AND r.isActive = true AND r.isDeleted = false")
    Optional<Role> findByName(@Param("name") String name);
}
