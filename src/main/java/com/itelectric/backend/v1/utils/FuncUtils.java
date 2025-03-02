package com.itelectric.backend.v1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itelectric.backend.v1.api.dto.response.BaseReadResponse;
import com.itelectric.backend.v1.domain.entity.AbstractAuditingEntity;
import com.itelectric.backend.v1.domain.entity.Address;
import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.enums.GeralStringEnuns;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.ForbiddenException;
import com.itelectric.backend.v1.domain.exception.UnexpectedException;
import com.itelectric.backend.v1.service.impl.AuditingService;
import jakarta.ws.rs.core.Response;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
public class FuncUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    public static void handlingKeycloakResponse(Response response) throws ConflictException, BusinessException, UnexpectedException, ForbiddenException {
        switch (response.getStatus()) {
            case 201: {
                return;
            }
            case 400: {
                throw new BusinessException(getKeycloakErrorMapErrorMessage(response));
            }
            case 403: {
                throw new ForbiddenException(getKeycloakErrorMapErrorMessage(response));
            }
            case 409: {
                throw new ConflictException(getKeycloakErrorMapErrorMessage(response));
            }
            default: {
                throw new UnexpectedException(getKeycloakErrorMapErrorMessage(response));
            }
        }
    }

    public static String getKeycloakResponseMessage(Response response) {
        if (response.hasEntity()) {
            try (Scanner scanner = new Scanner(response.readEntity(String.class)).useDelimiter("\\A")) {
                return scanner.hasNext() ? scanner.next() : "";
            }
        }
        return GeralStringEnuns.NO_MESSAGE_PROVIDED.name();
    }

    private static String getKeycloakErrorMapErrorMessage(Response response) throws UnexpectedException {
        try {
            String json = getKeycloakResponseMessage(response);
            if (json == null || json.isBlank() || GeralStringEnuns.NO_MESSAGE_PROVIDED.name().equals(json))
                return GeralStringEnuns.NO_MESSAGE_PROVIDED.name();
            return new ObjectMapper().readTree(json).path("errorMessage").asText("");
        } catch (Exception ex) {
            throw new UnexpectedException("Failed to parse Keycloak error message: " + ex.getMessage());
        }
    }

    public static String getRemoveRolePrefix(String role) {
        return role.substring(5); // ROLE_XTXTX
    }

    public static AbstractAuditingEntity setAuditFields(AbstractAuditingEntity entity) {
        AuditingService auditingService = context.getBean(AuditingService.class);
        String auditor = auditingService.getCurrentAuditor().get();
        entity.setCreatedBy(auditor);
        entity.setLastModifiedBy(auditor);
        return entity;
    }

    public static AbstractAuditingEntity setLastModifiedBy(AbstractAuditingEntity entity) {
        AuditingService auditingService = context.getBean(AuditingService.class);
        String auditor = auditingService.getCurrentAuditor().get();
        entity.setLastModifiedBy(auditor);
        return entity;
    }

    public static User getLoogedUser() {
        AuditingService auditingService = context.getBean(AuditingService.class);
        return auditingService.getLoggedUser();
    }

    public static <T> BaseReadResponse buildReadManyResponse(Page<T> entity, Object content) {
        return BaseReadResponse
                .builder()
                .numberOfElements(entity.getNumberOfElements())
                .pageNumer(entity.getPageable().getPageNumber())
                .pageSize(entity.getSize())
                .totalPages(entity.getTotalPages())
                .totalElements(entity.getTotalElements())
                .content(content).
                build();
    }

    public static BaseReadResponse buildReadOneResponse(Object content) {
        return BaseReadResponse
                .builder()
                .numberOfElements(1)
                .pageNumer(0)
                .pageSize(1)
                .totalPages(1)
                .totalElements(1L)
                .content(content).
                build();
    }

    public static String getFormatedAddress(Address address) {
        StringBuffer builder = new StringBuffer();
        appendIfNotEmpty(builder, address.getStreet());
        appendIfNotEmpty(builder, address.getNumber());
        appendIfNotEmpty(builder, address.getCity());
        appendIfNotEmpty(builder, address.getProvince());
        appendIfNotEmpty(builder, address.getCountry());

        if (address.getZipCode() != null && !address.getZipCode().isBlank()) {
            if (builder.length() > 0) builder.append(" - ");
            builder.append(address.getZipCode());
        }
        return builder.toString();
    }

    private static void appendIfNotEmpty(StringBuffer builder, String value) {
        if (value != null && !value.isBlank()) {
            if (builder.length() > 0) builder.append(", ");
            builder.append(value);
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        context = applicationContext;
    }
}
