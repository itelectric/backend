package com.itelectric.backend.v1.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class BaseReadResponse {
    private int pageNumer;
    private int pageSize;
    private int totalPages;
    private Long totalElements;
    private int numberOfElements;
    private Object content;
}
