package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.CreateServiceRequest;
import com.itelectric.backend.v1.api.dto.Response;
import com.itelectric.backend.v1.api.dto.UpdateServiceRequest;
import com.itelectric.backend.v1.domain.entity.ServiceManager;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.service.impl.ServiceManagerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Service")
@RequiredArgsConstructor
@RequestMapping("/api/v1/services")
public class ServiceManagerController {
    private final ServiceManagerService service;
    private final ModelMapper mapper;

    @Operation(summary = "Create Service")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> create(@Valid @RequestBody CreateServiceRequest request) throws ConflictException {
        ServiceManager serviceManager = this.mapper.map(request, ServiceManager.class);
        Duration estimatedTime = Duration.ofMinutes(0);
        if (!StringUtils.isEmpty(request.getEstimatedTime()))
            estimatedTime = Duration.parse(request.getEstimatedTime());
        serviceManager.setEstimatedTime(estimatedTime);
        this.service.create(serviceManager);
        Response response = new Response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                "Created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Create many Services")
    @PostMapping(path = "create-many", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> createMany(@RequestBody @NotEmpty List<@Valid CreateServiceRequest> request)
            throws ConflictException {
        List<ServiceManager> services = request.stream()
                .map(req -> this.mapper.map(req, ServiceManager.class))
                .collect(Collectors.toList());

        this.service.createMany(services);
        Response response = new Response(HttpStatus.CREATED.value(),
                HttpStatus.CREATED.name(),
                "Created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "List Services")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20O", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> readAll(@RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        Page<ServiceManager> list = this.service.readAll(pageNo, pageSize);
        Response response = new Response(HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                list);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Read by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20O", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> readById(@PathVariable("id") UUID id) throws NotFoundException {
        ServiceManager serviceManager = this.service.readByID(id);
        Response response = new Response(HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                serviceManager);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Service")
    @PutMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> update(@PathVariable("id") UUID id, @Valid @RequestBody UpdateServiceRequest request)
            throws ConflictException, NotFoundException {
        ServiceManager serviceManager = this.mapper.map(request, ServiceManager.class);
        serviceManager.setId(id);
        this.service.update(serviceManager);
        Response response = new Response(HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "OK.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Product")
    @DeleteMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> delete(@PathVariable("id") UUID id)
            throws NotFoundException {
        this.service.delete(id);
        Response response = new Response(HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                "OK.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
