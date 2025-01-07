package com.itelectric.backend.service.impl;

import org.springframework.data.domain.AuditorAware;

import java.util.Optional;

public class AuditingService implements AuditorAware<String> {

    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.of("Gervásio Artur DOmbo");
    }
}
