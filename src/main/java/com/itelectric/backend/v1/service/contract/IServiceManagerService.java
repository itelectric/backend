package com.itelectric.backend.v1.service.contract;


import com.itelectric.backend.v1.domain.entity.ServiceManager;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

//CRUD
public interface IServiceManagerService {
    void create(ServiceManager serviceManager) throws ConflictException;

    void createMany(List<ServiceManager> serviceManager) throws ConflictException;

    Page<ServiceManager> readAll(int pageNo, int pageSize);

    ServiceManager readByID(Integer id) throws NotFoundException;

    void update(ServiceManager serviceManager) throws ConflictException, NotFoundException;

    void delete(Integer id) throws NotFoundException;
}
