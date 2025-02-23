package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.enums.ProductType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface BaseProductRepository extends JpaRepository<BaseProduct, UUID> {
    @Query("SELECT b FROM BaseProduct b "
            + " WHERE UPPER(REPLACE(b.name, ' ', '')) = UPPER(REPLACE(:name, ' ', ''))"
            + " AND b.isDeleted = false"
            + " AND b.type = :type")
    Optional<BaseProduct> findByNameAndType(@Param("name") String name, @Param("type") ProductType type);

    @Query("SELECT b FROM BaseProduct b "
            + " WHERE UPPER(REPLACE(b.description, ' ', '')) = UPPER(REPLACE(:description, ' ', ''))"
            + " AND b.isDeleted = false"
            + " AND b.type = :type")
    Optional<BaseProduct> findByDescriptionFAndType(@Param("description") String description, @Param("type") ProductType type);

    @Query("SELECT b FROM BaseProduct b WHERE b.id = :id AND b.isDeleted = false")
    Optional<BaseProduct> findById(@Param("id") UUID id);
}
