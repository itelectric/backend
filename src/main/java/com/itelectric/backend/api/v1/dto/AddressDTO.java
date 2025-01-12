package com.itelectric.backend.api.v1.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class AddressDTO {
    private String street;
    private String number;
    private String city;
    private String province;
    private String country;
}
