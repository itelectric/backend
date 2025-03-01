package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.Quotation;
import com.itelectric.backend.v1.domain.report.QuotationReport;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QuotationRepository extends JpaRepository<Quotation, Integer> {
    @Query("SELECT NEW com.itelectric.backend.v1.domain.report.QuotationReport( " +
            " user.name, user.nuit, contact.phone, contact.email, " +
            " CONCAT(COALESCE(address.street, ''), ', ', COALESCE(address.number, ''), ', ', " +
            " COALESCE(address.city, ''), ', ', COALESCE(address.zipCode, ''), ', ', " +
            " COALESCE(address.province, ''), ', ', COALESCE(address.country, '')),qo.id,qo.createdDate) " +
            " FROM Quotation qo " +
            " JOIN qo.user user " +
            " JOIN user.contact contact " +
            " JOIN user.address address " +
            " WHERE qo.id = :quotationId" +
            " AND qo.user.id = :userId" +
            " AND qo.isDeleted = false")
    Optional<QuotationReport> findQuotationReport(@Param("quotationId") Integer quotationId, @Param("userId") UUID userId);

    @Query("SELECT q FROM Quotation q WHERE q.user.id = :userId AND q.isDeleted = false")
    Page<Quotation> findAll(@Param("userId") UUID userId, Pageable pageable);
}
