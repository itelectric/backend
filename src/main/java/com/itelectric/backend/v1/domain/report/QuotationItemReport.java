package com.itelectric.backend.v1.domain.report;

import com.itelectric.backend.v1.domain.enums.ProductType;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItemReport {
    private Integer baseProductId;
    private String baseProductName;
    private ProductType baseProductType;
    private BigDecimal baseProductUnitPrice;
    private String baseProductDescription;
    private boolean hasIVA;
    private BigDecimal IVA;
    private BigDecimal totalPrice;
    private BigDecimal totalPriceWithIVA;
    private BigDecimal totalIVA;
    private int quantity;


    public QuotationItemReport(Integer baseProductId, String baseProductName, ProductType baseProductType,
                               BigDecimal baseProductUnitPrice, String baseProductDescription, boolean hasIVA, int quantity) {
        this.baseProductId = baseProductId;
        this.baseProductName = baseProductName;
        this.baseProductType = baseProductType;
        this.baseProductUnitPrice = baseProductUnitPrice;
        this.baseProductDescription = baseProductDescription;
        this.hasIVA = hasIVA;
        this.quantity = quantity;
    }

    public String getProductTypeDsc() {
        return baseProductType.name();
    }
}
