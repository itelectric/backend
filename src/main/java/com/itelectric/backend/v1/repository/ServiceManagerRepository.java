package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.ServiceManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ServiceManagerRepository extends JpaRepository<ServiceManager, UUID> {
    @Query("SELECT s FROM ServiceManager s WHERE s.isDeleted = false")
    Page<ServiceManager> findALl(Pageable pageable);

    @Query("SELECT s FROM ServiceManager s WHERE s.id = :id AND s.isDeleted = false")
    Optional<ServiceManager> findById(@Param("id") UUID id);
}
