package com.itelectric.backend.v1.repository;

import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.domain.report.QuotationItemReport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuotationItemRepository extends JpaRepository<QuotationItem, Integer> {
    @Query("SELECT NEW com.itelectric.backend.v1.domain.report.QuotationItemReport(" +
            " bp.id,bp.name,bp.type,bp.price,bp.description,i.quantity) " +
            " FROM QuotationItem i " +
            " JOIN i.baseProduct bp " +
            " JOIN i.quotationOrder qo " +
            " WHERE qo.id = :quotationId")
    List<QuotationItemReport> findItemsReportByQuotationId(@Param("quotationId") Integer quotationId);

}
