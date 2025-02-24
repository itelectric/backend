package com.itelectric.backend.v1.service.impl;


import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.Product;
import com.itelectric.backend.v1.domain.enums.ProductType;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.repository.BaseProductRepository;
import com.itelectric.backend.v1.repository.ProductRepository;
import com.itelectric.backend.v1.service.contract.IProductService;
import com.itelectric.backend.v1.utils.FuncUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
@RequiredArgsConstructor
public class ProductService implements IProductService {
    private final BaseProductRepository baseProductRepository;
    private final ProductRepository productRepository;
    private final AuditingService auditingService;

    @Override
    public void create(Product product) throws ConflictException {
        product.setType(ProductType.PRODUCT);
        this.validateProductBeforeInsert(product);
        product.setDeleted(false);
        product.setAvailable(true);
        product = (Product) FuncUtils.setAuditFields(product);
        this.baseProductRepository.save(product);
    }

    @Override
    public void createMany(List<Product> product) throws ConflictException {
        for (Product item : product) {
            this.create(item);
        }
    }

    @Override
    public Page<Product> readAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return this.productRepository.findALl(pageable);
    }

    @Override
    public Product readByID(Integer id) throws NotFoundException {
        String errorMessage = MessageFormat.format("Could not find product with id:{0}", id);
        return this.productRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }

    @Override
    public void update(Product product) throws ConflictException, NotFoundException {
        product.setType(ProductType.PRODUCT);
        Product savedProduct = this.readByID(product.getId());
        this.validateProductBeforeInsert(product);
        this.updateProductData(savedProduct, product);
    }

    @Override
    public void delete(Integer id) throws NotFoundException {
        Product savedProduct = this.readByID(id);
        savedProduct.setDeleted(true);
        savedProduct = (Product) FuncUtils.setLastModifiedBy(savedProduct);
        this.productRepository.save(savedProduct);
    }

    private void validateProductBeforeInsert(Product product) throws ConflictException {
        String errorMessage = MessageFormat.format(
                """
                        A product with the same name ({0}),\
                        
                        type ({1}) and ({2}) already exists. \
                        
                        Please review the data and try again.""",
                product.getName(), product.getType(), product.getDescription()
        );

        if (!StringUtils.isEmpty(product.getName())) {
            String nameFilter = product.getName().trim().toUpperCase();
            Optional<BaseProduct> productExists = this.baseProductRepository.
                    findByNameAndType(nameFilter, ProductType.PRODUCT);
            if (productExists.isPresent()) throw new ConflictException(errorMessage);
        }

        if (!StringUtils.isEmpty(product.getDescription())) {
            String descriptionFilter = product.getDescription().trim().toUpperCase();
            Optional<BaseProduct> productExists = this.baseProductRepository.
                    findByDescriptionFAndType(descriptionFilter, ProductType.PRODUCT);
            if (productExists.isPresent()) throw new ConflictException(errorMessage);
        }
    }

    private void updateProductData(Product savedProduct, Product product) {
        Optional.ofNullable(product.getName())
                .filter(name -> !name.isEmpty() && !name.equals(savedProduct.getName()))
                .ifPresent(savedProduct::setName);

        Optional.ofNullable(product.getDescription())
                .filter(desc -> !desc.isEmpty() && !desc.equals(savedProduct.getDescription()))
                .ifPresent(savedProduct::setDescription);

        Optional.ofNullable(product.getPrice())
                .filter(price -> price.compareTo(BigDecimal.ZERO) != 0 && !price.equals(savedProduct.getPrice()))
                .ifPresent(savedProduct::setPrice);

        Optional.ofNullable(product.getStockQuantity())
                .filter(quantity -> quantity != 0 && !quantity.equals(savedProduct.getStockQuantity()))
                .ifPresent(savedProduct::setStockQuantity);

        Product finalProduct = savedProduct;
        finalProduct = (Product) FuncUtils.setLastModifiedBy(finalProduct);
        this.productRepository.save(finalProduct);
    }
}
