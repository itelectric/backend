package com.itelectric.backend.v1.api.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
public class CreateServiceRequest {
    @NotNull(message = "Name is required.")
    @NotBlank(message = "Name is required.")
    @Size(min = 6, message = "Name must have at least 6 characters.")
    private String name;

    @NotNull(message = "Description is required.")
    @NotBlank(message = "Description is required.")
    @Size(min = 15, message = "Description must at least 10 characters.")
    private String description;

    @NotNull(message = "Price is required.")
    @DecimalMin(value = "0.01", message = "Price must be greater than zero.")
    private BigDecimal price;

    private String estimatedTime;
}
