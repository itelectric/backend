package com.itelectric.backend.v1.service.contract;

import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.exception.*;
import com.itelectric.backend.v1.repository.UserRepository;
import com.itelectric.backend.v1.service.impl.AuditingService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.keycloak.common.VerificationException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.IOException;
import java.util.Optional;

@RequiredArgsConstructor
public abstract class GenericUserService {
    protected final UserRepository repository;
    protected final PasswordEncoder encoder;
    protected final AuditingService auditingService;
    private final IKeycloakCLIService keycloakCLIService;

    public void create(User user) throws ConflictException, BusinessException, UnexpectedException, ForbiddenException {
        Optional<User> savedUser = this.repository.findByNuit(user.getNuit());
        if (savedUser.isPresent())
            throw new ConflictException("We've found an account with this NUIT, please try to login.");

        savedUser = this.repository.findByUsername(user.getUsername());
        if (savedUser.isPresent())
            throw new ConflictException("Username already taken.");

        this.keycloakCLIService.createUser(user);
        String encodedPassword = this.encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setDeleted(false);
        //auditing user table
        user.setCreatedBy(auditingService.getCurrentAuditor().get());
        user.setLastModifiedBy(auditingService.getCurrentAuditor().get());
        this.repository.save(user);
    }

    public String login(String username, String password) throws UnauthorizedException {
        return this.keycloakCLIService.login(username, password);
    }

    public User getUserInfo(String token,
                              HttpServletRequest request,
                              HttpServletResponse response,
                              FilterChain filterChain) throws UnauthorizedException, VerificationException, ServletException, IOException {
        String username = this.keycloakCLIService.getUserInfo(token,request,response,filterChain);
        if(StringUtils.isEmpty(username)) throw new UnauthorizedException("Unauthorized");
        Optional<User> user = this.repository.findByUsername(username);
        if (user.isEmpty()) throw new UnauthorizedException("Unauthorized");
        return user.get();
    }
}
