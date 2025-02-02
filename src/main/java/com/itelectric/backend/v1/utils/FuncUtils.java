package com.itelectric.backend.v1.utils;

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
                throw new BusinessException(getKeycloakResponseMessage(response));
            }
            case 403: {
                throw new ForbiddenException(getKeycloakResponseMessage(response));
            }
            case 409: {
                throw new ConflictException(getKeycloakResponseMessage(response));
            }
            default: {
                throw new UnexpectedException(getKeycloakResponseMessage(response));
            }
        }
    }

    public static String getKeycloakResponseMessage(Response response) {
        if (response.hasEntity()) {
            try (Scanner scanner = new Scanner(response.readEntity(String.class)).useDelimiter("\\A")) {
                return scanner.hasNext() ? scanner.next() : "";
            }
        }
        return "No message provided.";
    }
}
