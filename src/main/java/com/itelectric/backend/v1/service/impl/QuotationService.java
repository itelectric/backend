package com.itelectric.backend.v1.service.impl;

import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.domain.entity.QuotationOrder;
import com.itelectric.backend.v1.domain.exception.DuplicationException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.repository.BaseProductRepository;
import com.itelectric.backend.v1.repository.QuotationItemRepository;
import com.itelectric.backend.v1.repository.QuotationOrderRepository;
import com.itelectric.backend.v1.service.contract.IQuotationService;
import com.itelectric.backend.v1.utils.FuncUtils;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class QuotationService implements IQuotationService {
    private final QuotationOrderRepository quotationOrderRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final BaseProductRepository baseProductRepository;

    @Override
    public void requestAQuotation(QuotationOrder quotationOrder) throws DuplicationException, NotFoundException {
        this.checkIfAllItemsExists(new ArrayList<>(quotationOrder.getItems()));
        this.checkDuplications(new ArrayList<>(quotationOrder.getItems()));
        List<QuotationItem> items = new ArrayList<>(quotationOrder.getItems());
        quotationOrder.setItems(null);
        quotationOrder.setDeleted(false);
        quotationOrder = (QuotationOrder) FuncUtils.setAuditFields(quotationOrder);
        QuotationOrder finalQuotationOrder = quotationOrderRepository.save(quotationOrder);

        items.forEach(item -> {
            item.setDeleted(false);
            item.setQuotationOrder(finalQuotationOrder);
            item = (QuotationItem) FuncUtils.setAuditFields(item);
            quotationItemRepository.save(item);
        });

        finalQuotationOrder.setItems(new HashSet<>(items));
        quotationOrderRepository.save(finalQuotationOrder);
    }

    private void checkDuplications(List<QuotationItem> items) throws DuplicationException {
        Map<UUID, BaseProduct> productMap = new HashMap<>();
        for (QuotationItem item : items) {
            UUID productId = item.getBaseProduct().getId();
            if (productMap.containsKey(productId)) {
                throw new DuplicationException("The item with the code " + productId
                        + " and name " + item.getBaseProduct().getName() + " is duplicated!");
            }
            productMap.put(productId, item.getBaseProduct());
        }
    }

    private void checkIfAllItemsExists(List<QuotationItem> items) throws NotFoundException {
        Optional<BaseProduct> baseProduct = Optional.empty();
        for (QuotationItem item : items) {
            UUID baseProductId = item.getBaseProduct().getId();
            baseProduct = this.baseProductRepository.findById(baseProductId);
            if (baseProduct.isEmpty())
                throw new NotFoundException("Could not find item in index: "+ items.indexOf(item)
                        + " with code: "+ baseProductId);
        }
    }
}
