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
public class ServiceManagerResponse {
    private Integer id;
    private String name;
    private String description;
    private String type;
    private BigDecimal price;
    private Long estimatedTime;
}
