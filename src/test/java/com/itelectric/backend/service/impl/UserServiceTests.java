package com.itelectric.backend.service.impl;

import com.itelectric.backend.domain.entity.Address;
import com.itelectric.backend.domain.entity.Contact;
import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.exception.ConflictException;
import com.itelectric.backend.mocks.UserMocksFactory;
import com.itelectric.backend.repository.UserRepository;
import com.itelectric.backend.service.contract.IUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private IUserService service;
    @MockitoBean
    private UserRepository repository;
    @MockitoBean
    private PasswordEncoder encoder;

    @Test
    @DisplayName("Should throw ConflictException if nuit is already in use on create user")
    void shouldThrowConflictExceptionIfNuitIsAlreadyInUseOnCreateUser() {
        Contact contact = UserMocksFactory.contactWithIdFactory();
        Address address = UserMocksFactory.addressWithIdFactory();
        User user = UserMocksFactory.userWithIdFactory(contact,address);

        Mockito.when(this.repository.findByNuit(user.getNuit())).thenReturn(Optional.of(user));

        Throwable exception = Assertions.catchThrowable(() -> this.service.create(user));

        Assertions.assertThat(exception).isInstanceOf(ConflictException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("We've found an account with this NUIT, please try to login.");
        Mockito.verify(this.repository, Mockito.times(1)).findByNuit(user.getNuit());
    }


    @Test
    @DisplayName("Should throw ConflictException if username is already in use on create user")
    void shouldThrowConflictExceptionIfUsernameIsAlreadyInUseOnCreateUser() {
        Contact contact = UserMocksFactory.contactWithIdFactory();
        Address address = UserMocksFactory.addressWithIdFactory();
        User user = UserMocksFactory.userWithIdFactory(contact,address);

        Mockito.when(this.repository.findByNuit(user.getUsername())).thenReturn(Optional.empty());
        Mockito.when(this.repository.findByUsername(user.getUsername())).thenReturn(Optional.of(user));

        Throwable exception = Assertions.catchThrowable(() -> this.service.create(user));

        Assertions.assertThat(exception).isInstanceOf(ConflictException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("Username already taken.");
        Mockito.verify(this.repository, Mockito.times(1)).findByNuit(user.getNuit());
        Mockito.verify(this.repository, Mockito.times(1)).findByUsername(user.getUsername());
    }

    @Test
    @DisplayName("Should encrypt password before save user info on create user")
    void shouldEncryptPasswordBeforeSaveUserInfoOnCreateUser() {
        Contact contact = UserMocksFactory.contactWithIdFactory();
        Address address = UserMocksFactory.addressWithIdFactory();
        User user = UserMocksFactory.userWithIdFactory(contact, address);
        String encodedPassword = UUID.randomUUID().toString();

        Mockito.when(this.repository.findByNuit(user.getUsername())).thenReturn(Optional.empty());
        Mockito.when(this.repository.findByUsername(user.getUsername())).thenReturn(Optional.empty());
        Mockito.when(this.encoder.encode(user.getPassword())).thenReturn(encodedPassword);

        Throwable exception = Assertions.catchThrowable(() -> this.service.create(user));

        Assertions.assertThat(exception).isInstanceOf(ConflictException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("Username already taken.");
        Mockito.verify(this.repository, Mockito.times(1)).findByNuit(user.getNuit());
        Mockito.verify(this.repository, Mockito.times(1)).findByUsername(user.getUsername());
        Mockito.verify(this.encoder, Mockito.times(1)).encode(user.getPassword());
    }
}
