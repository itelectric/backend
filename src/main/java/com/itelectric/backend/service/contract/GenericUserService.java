package com.itelectric.backend.service.contract;

import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.exception.ConflictException;
import com.itelectric.backend.repository.UserRepository;
import com.itelectric.backend.service.impl.AuditingService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

@RequiredArgsConstructor
public abstract class GenericUserService {
    protected final UserRepository repository;
    protected final PasswordEncoder encoder;
    protected  final AuditingService auditingService;

    public void create(User user) throws ConflictException {
        Optional<User> savedUser = this.repository.findByNuit(user.getNuit());
        if (savedUser.isPresent())
            throw new ConflictException("We've found an account with this NUIT, please try to login.");

        savedUser = this.repository.findByUsername(user.getUsername());
        if (savedUser.isPresent())
            throw new ConflictException("Username already taken.");

        String encodedPassword = this.encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setActive(true);
        user.setDeleted(false);
        //auditing user table
        user.setCreatedBy(auditingService.getCurrentAuditor().get());
        user.setLastModifiedBy(auditingService.getCurrentAuditor().get());
        this.repository.save(user);
    }
}
