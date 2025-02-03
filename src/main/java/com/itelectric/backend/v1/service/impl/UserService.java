package com.itelectric.backend.v1.service.impl;

import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.exception.UnauthorizedException;
import com.itelectric.backend.v1.repository.RoleRepository;
import com.itelectric.backend.v1.repository.UserRepository;
import com.itelectric.backend.v1.service.contract.GenericUserService;
import com.itelectric.backend.v1.service.contract.IKeycloakCLIService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.transaction.Transactional;
import org.keycloak.common.VerificationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@Transactional
public class UserService extends GenericUserService {

    public UserService(UserRepository repository,
                       PasswordEncoder encoder,
                       AuditingService auditingService,
                       RoleRepository roleRepository,
                       IKeycloakCLIService keycloakCLIService) {
        super(repository, encoder, auditingService, roleRepository, keycloakCLIService);
    }

    @Override
    public String login(String username, String password) throws UnauthorizedException {
        return super.login(username, password);
    }

    @Override
    public User getUserInfo(String token,
                            HttpServletRequest request,
                            HttpServletResponse response,
                            FilterChain filterChain) throws UnauthorizedException, VerificationException, ServletException, IOException {
        return super.getUserInfo(token, request, response, filterChain);
    }
}
