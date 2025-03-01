package com.itelectric.backend.v1.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItemResponse {
    private Integer baseProductId;
    private String baseProductName;
    private String baseProductType;
    private BigDecimal baseProductUnitPrice;
    private String baseProductDescription;
    private int quantity;
}
