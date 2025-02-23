package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.OrderQuotationRequest;
import com.itelectric.backend.v1.api.dto.Response;
import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.domain.entity.QuotationOrder;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.DuplicationException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.service.impl.QuotationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Quotation")
@RequiredArgsConstructor
@RequestMapping("/api/v1/quotations")
public class QuotationOrderController {
    private final QuotationService service;
    private final ModelMapper mapper;

    @Operation(summary = "Request a Quotation")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> create(@Valid @RequestBody OrderQuotationRequest request) throws ConflictException, DuplicationException, NotFoundException {
        QuotationOrder quotationOrder = this.mapper.map(request, QuotationOrder.class);
        List<QuotationItem> items = new ArrayList<>();
        items
                .addAll(request.getItems().stream()
                        .map(itemRequest -> QuotationItem.builder()
                                .quantity(itemRequest.getQuantity())
                                .baseProduct(BaseProduct.builder().id(itemRequest.getProductId()).build())
                                .build())
                        .collect(Collectors.toList()));
        quotationOrder.setItems(new HashSet<>(items));
        this.service.requestAQuotation(quotationOrder);
        Response response = new Response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                "Created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
