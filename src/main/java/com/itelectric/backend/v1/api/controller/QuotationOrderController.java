package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.OrderQuotationRequest;
import com.itelectric.backend.v1.api.dto.Response;
import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.domain.entity.QuotationOrder;
import com.itelectric.backend.v1.domain.exception.BusinessException;
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
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
    public ResponseEntity<Response> create(@Valid @RequestBody OrderQuotationRequest request)
            throws ConflictException, DuplicationException, NotFoundException, BusinessException {
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

    @Operation(summary = "Get Quotation Report")
    @GetMapping(path = "/{quotationId}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<byte[]> getQuotation(@PathVariable("quotationId") Integer quotationId) throws Exception {
        byte[] pdf = this.service.getQuotation(quotationId);
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String filename = "QuotationReport-" + timestamp + ".pdf";
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDisposition(ContentDisposition.attachment().filename(filename).build());
        headers.add(HttpHeaders.CACHE_CONTROL, "no-store, no-cache, must-revalidate, max-age=0");
        headers.add(HttpHeaders.PRAGMA, "no-cache");
        headers.add(HttpHeaders.EXPIRES, "0");
        return ResponseEntity.ok().headers(headers).body(pdf);
    }
}
