package com.itelectric.backend.v1.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class CreateAdminRequest {
    @NotNull(message = "Name is required.")
    @NotBlank(message = "Name is required.")
//    @Size(min = 6, message = "Name must have at least 6 characters.")
    private String name;

    @NotNull(message = "NUIT is required.")
    @NotBlank(message = "NUIT is required.")
    @Size(min = 15, max = 15, message = "Nuit must have exactly 15 characters.")
    private String nuit;

    private ContactRequest contact;
    private AddressRequest address;

    @NotNull(message = "Username is required.")
    @NotBlank(message = "Username is required.")
//    @Size(min = 6, message = "Username must have at least 6 characters.")
    private String username;

    @NotNull(message = "Password is required.")
    @NotBlank(message = "Password is required.")
//    @Size(min = 6, message = "Username must have at least 6 characters.")
    private String password;
}
