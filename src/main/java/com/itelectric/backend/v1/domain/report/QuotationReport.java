package com.itelectric.backend.v1.domain.report;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class QuotationReport {
    //quotation items
    List<QuotationItemReport> items;
    // client data
    private String name;
    private String nuit;
    private String cellPhone;
    private String email;
    private String address;
    //Quotation data
    private Integer quotationId;

    public QuotationReport(String name, String nuit, String cellPhone, String email, String address,Integer quotationId) {
        this.name = name;
        this.nuit = nuit;
        this.cellPhone = cellPhone;
        this.email = email;
        this.address = address;
        this.quotationId = quotationId;
    }
}
