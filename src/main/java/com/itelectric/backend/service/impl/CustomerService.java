package com.itelectric.backend.service.impl;

import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.enums.UserType;
import com.itelectric.backend.domain.exception.ConflictException;
import com.itelectric.backend.repository.UserRepository;
import com.itelectric.backend.service.contract.GenericUserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CustomerService extends GenericUserService {

    public CustomerService(UserRepository repository, PasswordEncoder encoder, AuditingService auditingService) {
        super(repository, encoder, auditingService);
    }

    @Override
    public void create(User user) throws ConflictException {
        user.setType(UserType.CUSTOMER);
        super.create(user);
    }
}
