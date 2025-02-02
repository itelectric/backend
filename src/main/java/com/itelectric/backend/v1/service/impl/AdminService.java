package com.itelectric.backend.v1.service.impl;

import com.itelectric.backend.v1.domain.entity.User;
import com.itelectric.backend.v1.domain.enums.UserType;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.ForbiddenException;
import com.itelectric.backend.v1.domain.exception.UnexpectedException;
import com.itelectric.backend.v1.repository.UserRepository;
import com.itelectric.backend.v1.service.contract.GenericUserService;
import com.itelectric.backend.v1.service.contract.IKeycloakCLIService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class AdminService extends GenericUserService {

    public AdminService(UserRepository repository, PasswordEncoder encoder, AuditingService auditingService, IKeycloakCLIService keycloakCLIService) {
        super(repository, encoder, auditingService, keycloakCLIService);
    }

    @Override
    public void create(User user) throws ConflictException, BusinessException, UnexpectedException, ForbiddenException {
        user.setType(UserType.ADMIN);
        super.create(user);
    }
}
