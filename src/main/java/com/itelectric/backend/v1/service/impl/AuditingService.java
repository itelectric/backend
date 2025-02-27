package com.itelectric.backend.v1.service.impl;

import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.enums.Roles;
import com.itelectric.backend.v1.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AuditingService implements AuditorAware<String> {
    private final UserRepository userRepository;

    @Override
    public Optional<String> getCurrentAuditor() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt jwt) {
            String preferredUsername = jwt.getClaimAsString("preferred_username");
            return Optional.of(preferredUsername.toUpperCase());
        }
        return Optional.of(Roles.SYSTEM.name());
    }

    public User getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated() && authentication.getPrincipal() instanceof Jwt jwt) {
            String preferredUsername = jwt.getClaimAsString("preferred_username").toUpperCase();
            return userRepository.findByUsername(preferredUsername).orElse(null);
        }
        return null;
    }
}
