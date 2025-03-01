package com.itelectric.backend.v1.api.mapper;

import com.itelectric.backend.v1.api.dto.request.OrderQuotationRequest;
import com.itelectric.backend.v1.api.dto.request.QuotationItemRequest;
import com.itelectric.backend.v1.api.dto.response.QuotationItemResponse;
import com.itelectric.backend.v1.api.dto.response.QuotationResponse;
import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.Quotation;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.utils.FuncUtils;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class QuotationMapper {
    public static QuotationResponse mapToQuotationResponse(Quotation entity) {
        List<QuotationItemResponse> itemResponses = mapToListQuotationItemResponse(new ArrayList<>(entity.getItems()));
        return QuotationResponse
                .builder()
                .name(entity.getUser().getName())
                .nuit(entity.getUser().getNuit())
                .cellPhone(entity.getUser().getContact().getPhone())
                .email(entity.getUser().getContact().getEmail())
                .address(FuncUtils.getFormatedAddress(entity.getUser().getAddress()))
                .quotationId(entity.getId())
                .issueDate(entity.getCreatedDate())
                .items(itemResponses)
                .build();
    }

    public static List<QuotationResponse> mapToListQuotationResponse(List<Quotation> list) {
        return list.stream()
                .map(QuotationMapper::mapToQuotationResponse)
                .collect(Collectors.toList());
    }

    public static QuotationItemResponse mapToQuotationItemResponse(QuotationItem entity) {
        return QuotationItemResponse
                .builder()
                .baseProductId(entity.getBaseProduct().getId())
                .baseProductName(entity.getBaseProduct().getName())
                .baseProductType(entity.getBaseProduct().getType().name())
                .baseProductUnitPrice(entity.getBaseProduct().getPrice())
                .baseProductDescription(entity.getBaseProduct().getDescription())
                .quantity(entity.getQuantity())
                .build();
    }

    public static List<QuotationItemResponse> mapToListQuotationItemResponse(List<QuotationItem> list) {
        return list.stream()
                .map(QuotationMapper::mapToQuotationItemResponse)
                .collect(Collectors.toList());
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
                .collect(Collectors.toList());
    }
}
