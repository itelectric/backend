package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.QuotationOrder;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface QuotationOrderRepository extends JpaRepository<QuotationOrder, UUID> {
}
