package com.itelectric.backend.v1.service.contract;

import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.exception.*;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.keycloak.common.VerificationException;

import java.io.IOException;

public interface IKeycloakCLIService {
    public void createUser(User user) throws ConflictException, BusinessException, UnexpectedException, ForbiddenException;

    public String login(String username, String password) throws UnauthorizedException;

    public String getUserInfo(String token,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              FilterChain filterChain) throws UnauthorizedException, ServletException, IOException;
}
