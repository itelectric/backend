package com.itelectric.backend.v1.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContactRequest {
    @NotNull(message = "Phone is  required.")
    @NotBlank(message = "Phone is required.")
    private String phone;

    @NotNull(message = "Email is required.")
    @NotBlank(message = "Email is required.")
    private String email;
}
