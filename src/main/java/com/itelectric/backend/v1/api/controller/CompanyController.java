package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.request.CreateCompanyRequest;
import com.itelectric.backend.v1.api.dto.response.Response;
import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.ForbiddenException;
import com.itelectric.backend.v1.domain.exception.UnexpectedException;
import com.itelectric.backend.v1.service.impl.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

@RestController
@Tag(name = "Company")
@RequiredArgsConstructor
@RequestMapping("/api/v1/companies")
public class CompanyController {
    private final CompanyService service;
    private final ModelMapper mapper;

    @Operation(summary = "Register company")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> create(@Valid @RequestBody CreateCompanyRequest request) throws ConflictException, BusinessException, UnexpectedException, ForbiddenException {
        User user = this.mapper.map(request, User.class);
        this.service.create(user);
        Response response = new Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), "Created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
