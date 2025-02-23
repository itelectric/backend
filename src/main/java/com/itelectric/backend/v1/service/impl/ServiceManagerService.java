package com.itelectric.backend.v1.service.impl;


import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.ServiceManager;
import com.itelectric.backend.v1.domain.enums.ProductType;
import com.itelectric.backend.v1.domain.exception.ConflictException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.repository.BaseProductRepository;
import com.itelectric.backend.v1.repository.ServiceManagerRepository;
import com.itelectric.backend.v1.service.contract.IServiceManagerService;
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
import java.time.Duration;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ServiceManagerService implements IServiceManagerService {
    private final BaseProductRepository baseProductRepository;
    private final ServiceManagerRepository serviceManagerRepository;
    private final AuditingService auditingService;

    @Override
    public void create(ServiceManager serviceManager) throws ConflictException {
        serviceManager.setType(ProductType.SERVICE);
        this.validateProductBeforeInsert(serviceManager);
        serviceManager.setDeleted(false);
        serviceManager.setAvailable(true);
        //auditing
        serviceManager.setCreatedBy(auditingService.getCurrentAuditor().get());
        serviceManager.setLastModifiedBy(auditingService.getCurrentAuditor().get());
        this.baseProductRepository.save(serviceManager);
    }

    @Override
    public void createMany(List<ServiceManager> serviceManager) throws ConflictException {
        for (ServiceManager item : serviceManager) {
            this.create(item);
        }
    }

    @Override
    public Page<ServiceManager> readAll(int pageNo, int pageSize) {
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return this.serviceManagerRepository.findALl(pageable);
    }

    @Override
    public ServiceManager readByID(UUID id) throws NotFoundException {
        String errorMessage = MessageFormat.format("Could not find service with id:{0}", id);
        return this.serviceManagerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException(errorMessage));
    }

    @Override
    public void update(ServiceManager serviceManager) throws ConflictException, NotFoundException {
        serviceManager.setType(ProductType.SERVICE);
        ServiceManager savedProduct = this.readByID(serviceManager.getId());
        this.validateProductBeforeInsert(serviceManager);
        this.updateProductData(savedProduct, serviceManager);
    }

    @Override
    public void delete(UUID id) throws NotFoundException {
        ServiceManager serviceManager = this.readByID(id);
        serviceManager.setDeleted(true);
        serviceManager = (ServiceManager) FuncUtils.setLastModifiedBy(serviceManager);
        this.serviceManagerRepository.save(serviceManager);
    }

    private void validateProductBeforeInsert(ServiceManager serviceManager) throws ConflictException {
        String errorMessage = MessageFormat.format(
                """
                        A service with the same name ({0}),\
                        
                        type ({1}) and ({2}) already exists. \
                        
                        Please review the data and try again.""",
                serviceManager.getName(), serviceManager.getType(), serviceManager.getDescription()
        );

        if (!StringUtils.isEmpty(serviceManager.getName())) {
            String nameFilter = serviceManager.getName().trim().toUpperCase();
            Optional<BaseProduct> productExists = this.baseProductRepository.
                    findByNameAndType(nameFilter, ProductType.SERVICE);
            if (productExists.isPresent()) throw new ConflictException(errorMessage);
        }

        if (!StringUtils.isEmpty(serviceManager.getDescription())) {
            String descriptionFilter = serviceManager.getDescription().trim().toUpperCase();
            Optional<BaseProduct> productExists = this.baseProductRepository.
                    findByDescriptionFAndType(descriptionFilter, ProductType.SERVICE);
            if (productExists.isPresent()) throw new ConflictException(errorMessage);
        }
    }

    private void updateProductData(ServiceManager savedServiceManager, ServiceManager serviceManager) {
        Optional.ofNullable(serviceManager.getName())
                .filter(name -> !name.isEmpty() && !name.equals(savedServiceManager.getName()))
                .ifPresent(savedServiceManager::setName);

        Optional.ofNullable(serviceManager.getDescription())
                .filter(desc -> !desc.isEmpty() && !desc.equals(savedServiceManager.getDescription()))
                .ifPresent(savedServiceManager::setDescription);

        Optional.ofNullable(serviceManager.getPrice())
                .filter(price -> price.compareTo(BigDecimal.ZERO) != 0 && !price.equals(savedServiceManager.getPrice()))
                .ifPresent(savedServiceManager::setPrice);

        Optional.ofNullable(serviceManager.getEstimatedTime())
                .filter(duration -> !duration.equals(Duration.ZERO) && !duration.equals(savedServiceManager.getEstimatedTime()))
                .ifPresent(savedServiceManager::setEstimatedTime);

        ServiceManager finalServiceManager = savedServiceManager;
        finalServiceManager = (ServiceManager) FuncUtils.setLastModifiedBy(finalServiceManager);
        this.serviceManagerRepository.save(finalServiceManager);
    }
}
