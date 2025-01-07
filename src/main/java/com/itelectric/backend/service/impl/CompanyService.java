package com.itelectric.backend.service.impl;

import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.enums.UserType;
import com.itelectric.backend.domain.exception.ConflictException;
import com.itelectric.backend.repository.UserRepository;
import com.itelectric.backend.service.contract.GenericUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Transactional
public class CompanyService extends GenericUserService {
    @Autowired
    private UserRepository repository;
    @Autowired
    private PasswordEncoder encoder;

    public CompanyService(UserRepository repository, PasswordEncoder encoder) {
        super(repository, encoder);
    }

    @Override
    public void create(User user) throws ConflictException {
        user.setType(UserType.COMPANY);
        super.create(user);
    }
}
