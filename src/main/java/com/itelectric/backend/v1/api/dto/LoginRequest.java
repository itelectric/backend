package com.itelectric.backend.v1.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "Username is required.")
    @NotBlank(message = "Username is required.")
    private String username;

    @NotNull(message = "Password is required.")
    @NotBlank(message = "Password is required.")
    private String password;
}
