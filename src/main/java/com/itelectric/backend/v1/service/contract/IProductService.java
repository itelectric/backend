package com.itelectric.backend.v1.service.contract;


import com.itelectric.backend.v1.domain.entity.Product;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import org.springframework.data.domain.Page;

import java.util.List;

//CRUD
public interface IProductService {
    void create(Product product) throws ConflictException;

    void createMany(List<Product> product) throws ConflictException;

    Page<Product> readAll(int pageNo, int pageSize);

    Product readByID(Integer id) throws NotFoundException;

    void update(Product product) throws ConflictException, NotFoundException;

    void delete(Integer id) throws NotFoundException;
}
