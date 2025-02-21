package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ServiceRepository extends JpaRepository<Product, UUID> {
}
