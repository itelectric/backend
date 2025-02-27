package com.itelectric.backend.v1.domain.report;

import com.itelectric.backend.v1.domain.enums.ProductType;
import lombok.*;

import java.math.BigDecimal;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class QuotationItemReport {
    private Integer baseProductId;
    private String baseProductName;
    private ProductType baseProductType;
    private BigDecimal baseProductUnitPrice;
    private String baseProductDescription;
    private int quantity;

    public String getProductTypeDsc() {
        return baseProductType.name();
    }
}
