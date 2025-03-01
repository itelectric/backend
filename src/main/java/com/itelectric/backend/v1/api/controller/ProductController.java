package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.request.CreateProductRequest;
import com.itelectric.backend.v1.api.dto.request.UpdateProductRequest;
import com.itelectric.backend.v1.api.dto.response.BaseReadResponse;
import com.itelectric.backend.v1.api.dto.response.ProductResponse;
import com.itelectric.backend.v1.api.dto.response.Response;
import com.itelectric.backend.v1.domain.entity.Product;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.service.impl.ProductService;
import com.itelectric.backend.v1.utils.FuncUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@Tag(name = "Product")
@RequiredArgsConstructor
@RequestMapping("/api/v1/products")
public class ProductController {
    private final ProductService service;
    private final ModelMapper mapper;

    @Operation(summary = "Create Product")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> create(@Valid @RequestBody CreateProductRequest request) throws ConflictException {
        Product product = this.mapper.map(request, Product.class);
        this.service.create(product);
        Response response = new Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), "Created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "Create many Products")
    @PostMapping(path = "create-many", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "CREATED"),
            @ApiResponse(responseCode = "400", description = "BAD_REQUEST"),
            @ApiResponse(responseCode = "401", description = "UNAUTHORIZED"),
            @ApiResponse(responseCode = "409", description = "CONFLICT"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<Response> createMany(@RequestBody @NotEmpty List<@Valid CreateProductRequest> request)
            throws ConflictException {
        List<Product> products = request.stream()
                .map(req -> this.mapper.map(req, Product.class))
                .collect(Collectors.toList());
        this.service.createMany(products);
        Response response = new Response(HttpStatus.CREATED.value(), HttpStatus.CREATED.name(), "Created.");
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @Operation(summary = "List Products")
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20O", description = "OK"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> readAll(@RequestParam(defaultValue = "0") int pageNo,
                                            @RequestParam(defaultValue = "10") int pageSize) {
        Page<Product> list = this.service.readAll(pageNo, pageSize);
        List<ProductResponse> productResponses = list.stream()
                .map(product -> mapper.map(product, ProductResponse.class))
                .collect(Collectors.toList());
        BaseReadResponse baseResponse = FuncUtils.buildReadManyResponse(list, productResponses);
        Response response = new Response(HttpStatus.OK.value(), HttpStatus.OK.name(), baseResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Read by id")
    @GetMapping(path = "/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20O", description = "OK"),
            @ApiResponse(responseCode = "404", description = "NOT_FOUND"),
            @ApiResponse(responseCode = "500", description = "INTERNAL_SERVER_ERROR")
    })
    public ResponseEntity<Response> readById(@PathVariable("id") Integer id) throws NotFoundException {
        Product product = this.service.readByID(id);
        ProductResponse productResponse = mapper.map(product, ProductResponse.class);
        BaseReadResponse readResponse = FuncUtils.buildReadOneResponse(productResponse);
        Response response = new Response(HttpStatus.OK.value(), HttpStatus.OK.name(), readResponse);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @Operation(summary = "Update Product")
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
    public ResponseEntity<Response> update(@PathVariable("id") Integer id, @Valid @RequestBody UpdateProductRequest request)
            throws ConflictException, NotFoundException {
        Product product = this.mapper.map(request, Product.class);
        product.setId(id);
        this.service.update(product);
        Response response = new Response(HttpStatus.OK.value(), HttpStatus.OK.name(), "OK.");
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
    public ResponseEntity<Response> delete(@PathVariable("id") Integer id)
            throws NotFoundException {
        this.service.delete(id);
        Response response = new Response(HttpStatus.OK.value(), HttpStatus.OK.name(), "OK.");
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
