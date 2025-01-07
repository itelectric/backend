package com.itelectric.backend.service.contract;

import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.exception.ConflictException;
import com.itelectric.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public abstract class GenericUserService {
    private final UserRepository repository;
    private final PasswordEncoder encoder;

    public void create(User user) throws ConflictException {
        Optional<User> savedUser = this.repository.findByNuit(user.getNuit());
        if (savedUser.isPresent())
            throw new ConflictException("We've found an account with this NUIT, please try to login.");

        savedUser = this.repository.findByUsername(user.getUsername());
        if (savedUser.isPresent())
            throw new ConflictException("Username already taken.");

        String encodedPassword = this.encoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        this.repository.save(user);
    }
}
