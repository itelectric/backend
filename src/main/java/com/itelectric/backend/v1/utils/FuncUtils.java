package com.itelectric.backend.v1.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.itelectric.backend.v1.domain.enums.GeralEnuns;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.ForbiddenException;
import com.itelectric.backend.v1.domain.exception.UnexpectedException;
import jakarta.ws.rs.core.Response;

import java.util.Scanner;

public class FuncUtils {
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
        return GeralEnuns.NO_MESSAGE_PROVIDED.name();
    }

    private static String getKeycloakErrorMapErrorMessage(Response response) throws UnexpectedException {
        try {
            String json = getKeycloakResponseMessage(response);
            if (json == null || json.isBlank() || GeralEnuns.NO_MESSAGE_PROVIDED.name().equals(json))
                return GeralEnuns.NO_MESSAGE_PROVIDED.name();
            return new ObjectMapper().readTree(json).path("errorMessage").asText("");
        } catch (Exception ex) {
            throw new UnexpectedException("Failed to parse Keycloak error message: " + ex.getMessage());
        }
    }

    public static String getRemoveRolePrefix(String role) {
        return role.substring(5); // ROLE_XTXTX
    }
}
