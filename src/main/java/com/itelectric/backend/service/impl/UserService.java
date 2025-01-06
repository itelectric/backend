package com.itelectric.backend.service.impl;

import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.exception.ConflictException;
import com.itelectric.backend.repository.UserRepository;
import com.itelectric.backend.service.contract.IUserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
public class UserService implements IUserService {
    @Autowired
    private UserRepository repository;

    @Override
    public void create(User user) throws ConflictException {
        Optional<User> savedUser = this.repository.findByNuit(user.getNuit());
        if (savedUser.isPresent())
            throw new ConflictException("We've found an account with this NUIT, please try to login.");

        savedUser = this.repository.findByUsername(user.getUsername());
        if (savedUser.isPresent())
            throw new ConflictException("Username already taken.");

    }
}
