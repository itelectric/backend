package com.itelectric.backend.v1.api.mapper;

import com.itelectric.backend.v1.api.dto.request.OrderQuotationRequest;
import com.itelectric.backend.v1.api.dto.request.QuotationItemRequest;
import com.itelectric.backend.v1.api.dto.response.QuotationResponse;
import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.Quotation;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.utils.Tax;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class QuotationMapper {

    public static QuotationResponse mapToQuotationResponse(Quotation entity) {
        return Tax.calculateIVAAndMapToQuotationResponse(entity);
    }

    public static List<QuotationResponse> mapToListQuotationResponse(List<Quotation> list) {
        return list.stream()
                .map(QuotationMapper::mapToQuotationResponse)
                .toList();
    }

    public static Quotation mapToQuotationEntity(OrderQuotationRequest request) {
        List<QuotationItem> list = mapToListQuotationItemEntity(request.getItems());
        Set<QuotationItem> items = new HashSet<>(list);
        return Quotation
                .builder()
                .description(request.getDescription())
                .deadlineAnswer(request.getDeadlineAnswer())
                .description(request.getDescription())
                .items(items)
                .build();
    }

    public static QuotationItem mapToQuotationItemEntity(QuotationItemRequest request) {
        return QuotationItem
                .builder()
                .quantity(request.getQuantity())
                .baseProduct(BaseProduct.builder().id(request.getProductId()).build())
                .build();
    }

    public static List<QuotationItem> mapToListQuotationItemEntity(List<QuotationItemRequest> list) {
        return list.stream()
                .map(QuotationMapper::mapToQuotationItemEntity)
                .toList();
    }
}
