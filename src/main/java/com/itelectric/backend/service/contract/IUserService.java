package com.itelectric.backend.service.contract;

import com.itelectric.backend.domain.entity.User;
import com.itelectric.backend.domain.exception.ConflictException;

public interface IUserService {
    void create(User user) throws ConflictException;
}