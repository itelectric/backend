package com.itelectric.backend.service.impl;

import com.itelectric.backend.domain.entity.Address;
import com.itelectric.backend.domain.entity.Contact;
import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.enums.UserType;
import com.itelectric.backend.domain.exception.ConflictException;
import com.itelectric.backend.repository.UserRepository;
import com.itelectric.backend.service.contract.IUserService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;
import java.util.UUID;

@SpringBootTest
public class UserServiceTests {
    @Autowired
    private IUserService service;
    @MockitoBean
    private UserRepository repository;

    @Test
    @DisplayName("Should throw ConflictException if nuit is already in use on create user")
    void shouldThrowConflictExceptionIfNuitIsAlreadyInUseOnCreateUser() {
        Contact contact = Contact
                .builder()
                .id(UUID.randomUUID())
                .phone("any_phone")
                .email("any_email")
                .build();

        Address address = Address
                .builder()
                .street("any_street")
                .number("any_number")
                .city("any_city")
                .province("any_province")
                .country("country")
                .build();

        User user = User
                .builder()
                .id(UUID.randomUUID())
                .name("any_name")
                .nuit(UUID.randomUUID().toString())
                .contact(contact)
                .address(address)
                .username("any_username")
                .password("any_password")
                .type(UserType.CLIENT)
                .build();

        User savedUser = user;
        savedUser.setName("Any_name");

        Mockito.when(this.repository.findByNuit(user.getNuit())).thenReturn(Optional.of(savedUser));

        Throwable exception = Assertions.catchThrowable(() -> this.service.create(user));

        Assertions.assertThat(exception).isInstanceOf(ConflictException.class);
        Assertions.assertThat(exception.getMessage()).isEqualTo("We've found an account with this NUIT, please try to login.");
        Mockito.verify(repository, Mockito.times(1)).findByNuit(user.getNuit());
    }
}
