package com.itelectric.backend.v1.api.dto;

import com.itelectric.backend.v1.service.impl.QuotationItemRequest;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class OrderQuotationRequest {
    private String description;
    private LocalDate deadlineAnswer;

    @NotNull(message = "Items are required.")
    @Size(min = 1, message = "There must be at least one item.")
    private List<QuotationItemRequest> items;
}
