package com.itelectric.backend.v1.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationResponse {
    List<QuotationItemResponse> items;
    // customer data
    private String name;
    private String nuit;
    private String cellPhone;
    private String email;
    private String address;
    //Quotation data
    private Integer quotationId;
    private Instant issueDate;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceWithIVA;
    private BigDecimal totalIVA;
}
