package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.request.OrderQuotationRequest;
import com.itelectric.backend.v1.api.dto.response.BaseReadResponse;
import com.itelectric.backend.v1.api.dto.response.QuotationResponse;
import com.itelectric.backend.v1.api.dto.response.Response;
import com.itelectric.backend.v1.api.mapper.QuotationMapper;
import com.itelectric.backend.v1.domain.entity.Quotation;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.DuplicationException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.service.impl.QuotationService;
import com.itelectric.backend.v1.utils.FuncUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RestController
@Tag(name = "Quotation")
@RequiredArgsConstructor
@RequestMapping("/api/v1/quotations")
public class QuotationController {
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
        Quotation quotation = QuotationMapper.mapToQuotationEntity(request);
        this.service.requestQuotation(quotation);
        Response response = new Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), "Created.");
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

    @Operation(summary = "List all Quotations")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "CREATED"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> readAll(@RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize) throws Exception {
        Page<Quotation> list = this.service.readAll(pageNo, pageSize);
        List<QuotationResponse> quotationResponses = QuotationMapper.mapToListQuotationResponse(list.getContent());
        BaseReadResponse baseResponse = FuncUtils.buildReadManyResponse(list, quotationResponses);
        Response response = new Response(HttpStatus.OK.value(), HttpStatus.OK.name(), baseResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
