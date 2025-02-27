package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.QuotationOrder;
import com.itelectric.backend.v1.domain.report.QuotationReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuotationOrderRepository extends JpaRepository<QuotationOrder, Integer> {
    @Query("SELECT NEW com.itelectric.backend.v1.domain.report.QuotationReport( " +
            " user.name, user.nuit, contact.phone, contact.email, " +
            " CONCAT(COALESCE(address.street, ''), ', ', COALESCE(address.number, ''), ', ', " +
            " COALESCE(address.city, ''), ', ', COALESCE(address.zipCode, ''), ', ', " +
            " COALESCE(address.province, ''), ', ', COALESCE(address.country, '')),qo.id,qo.createdDate) " +
            " FROM QuotationOrder qo " +
            " JOIN qo.user user " +
            " JOIN user.contact contact " +
            " JOIN user.address address " +
            " WHERE qo.id = :quotationId" +
            " AND qo.user.id = :userId")
    Optional<QuotationReport> findQuotationReport(@Param("quotationId") Integer quotationId, @Param("userId") UUID userId);

}
