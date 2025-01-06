package com.itelectric.backend.mocks;

import com.itelectric.backend.domain.entity.Address;
import com.itelectric.backend.domain.entity.Contact;
import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.enums.UserType;

import java.util.UUID;

public class UserMocksFactory {
    public static Contact contactWithIdFactory() {
        return Contact
                .builder()
                .id(UUID.randomUUID())
                .phone("any_phone")
                .email("any_email")
                .build();
    }

    public static Address addressWithIdFactory() {
        return Address
                .builder()
                .street("any_street")
                .number("any_number")
                .city("any_city")
                .province("any_province")
                .country("country")
                .build();
    }

    public static User userWithIdFactory(Contact contact, Address address) {
       return User
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
    }
}
