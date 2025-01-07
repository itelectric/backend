package com.itelectric.backend.api.dto;

public record Response(int code, String status, Object body) {
}
