package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.CreateAdminRequest;
import com.itelectric.backend.v1.api.dto.Response;
import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.ForbiddenException;
import com.itelectric.backend.v1.domain.exception.UnexpectedException;
import com.itelectric.backend.v1.service.impl.AdminService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
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

@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)

@RestController
@Tag(name = "Admin")
@RequiredArgsConstructor
@RequestMapping("/api/v1/admins")
public class AdminController {
    private final AdminService service;
    private final ModelMapper mapper;

    @Operation(summary = "Register Admin")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request happened"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> create(@Valid @RequestBody CreateAdminRequest request) throws ConflictException, BusinessException, UnexpectedException, ForbiddenException {
        User user = this.mapper.map(request, User.class);
        this.service.create(user);
        Response response = new Response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                "Created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
