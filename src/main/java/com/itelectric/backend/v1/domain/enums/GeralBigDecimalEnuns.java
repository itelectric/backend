package com.itelectric.backend.v1.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@AllArgsConstructor
public enum GeralBigDecimalEnuns {
    IVA_RATE(new BigDecimal(0.17));

    private BigDecimal value;
}
