package com.itelectric.backend.api.v1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ContactDTO {
    @NotNull(message = "Phone is  required.")
    @NotBlank(message = "Phone is required.")
    private String phone;

    @NotNull(message = "Email is required.")
    @NotBlank(message = "Email is required.")
    private String email;
}
