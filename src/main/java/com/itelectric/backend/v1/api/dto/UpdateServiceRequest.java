package com.itelectric.backend.v1.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.Duration;

@Data
@Builder
@AllArgsConstructor
public class UpdateServiceRequest {
    @Size(min = 6, message = "Name must have at least 6 characters.")
    private String name;

    @Size(min = 15, message = "Description must at least 10 characters.")
    private String description;

    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    private BigDecimal price;

    private Duration estimatedTime;
}
