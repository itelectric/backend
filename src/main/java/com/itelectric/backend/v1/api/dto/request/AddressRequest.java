package com.itelectric.backend.v1.api.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddressRequest {
    @NotNull(message = "Phone is  required.")
    @NotBlank(message = "Phone is required.")
    private String street;

    @NotNull(message = "Phone is  required.")
    @NotBlank(message = "Phone is required.")
    private String number;

    @NotNull(message = "Phone is  required.")
    @NotBlank(message = "Phone is required.")
    private String city;

    @NotNull(message = "Phone is  required.")
    @NotBlank(message = "Phone is required.")
    private String province;

    @NotNull(message = "Phone is  required.")
    @NotBlank(message = "Phone is required.")
    private String country;
}
