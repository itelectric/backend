package com.itelectric.backend.v1.domain.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Supplier {
    NUIT("123456789085763"),
    EMAIL("ITELETRIC@GMAIL.COM"),
    CONTACT("+258 (84) 7865-460"),
    ADDRESS("RUA DUQUE DE CAIXIAS , 1098, SOMERSHILD, MAPUTO, MOZAMBIQUE"),
    ;
    private String value;
}
