package com.itelectric.backend.api.v1.dto;

public record Response(int code, String status, Object body) {
}
