package com.itelectric.backend.v1.api.controller;

import com.itelectric.backend.v1.api.dto.LoginRequest;
import com.itelectric.backend.v1.api.dto.Response;
import com.itelectric.backend.v1.domain.exception.UnauthorizedException;
import com.itelectric.backend.v1.service.impl.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Tag(name = "User")
@RequiredArgsConstructor
@RequestMapping("/api/v1/users")
public class UserController {
    private final UserService service;

    @Operation(summary = "User Login")
    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiResponses(value = {
            @ApiResponse(responseCode = "20O", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request happened"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "500", description = "An unexpected error occurred")
    })
    public ResponseEntity<Response> create(@Valid @RequestBody LoginRequest request) throws UnauthorizedException {
        String token = this.service.login(request.getUsername(), request.getPassword());
        Response response = new Response(HttpStatus.OK.value(),
                HttpStatus.OK.name(),
                token);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
