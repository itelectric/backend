package com.itelectric.backend.v1.domain.entity;

import jakarta.persistence.Embeddable;
import lombok.*;

@Getter
@Setter
@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    private String street;
    private String number;
    private String city;
    private String zipCode;
    private String province;
    private String country;
}