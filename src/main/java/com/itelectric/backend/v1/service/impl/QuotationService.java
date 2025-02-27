package com.itelectric.backend.v1.service.impl;

import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.domain.entity.QuotationOrder;
import com.itelectric.backend.v1.domain.enums.Supplier;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.DuplicationException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.domain.report.QuotationItemReport;
import com.itelectric.backend.v1.domain.report.QuotationReport;
import com.itelectric.backend.v1.repository.BaseProductRepository;
import com.itelectric.backend.v1.repository.QuotationItemRepository;
import com.itelectric.backend.v1.repository.QuotationOrderRepository;
import com.itelectric.backend.v1.service.contract.IQuotationService;
import com.itelectric.backend.v1.utils.FuncUtils;
import com.itelectric.backend.v1.utils.Jasper;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class QuotationService implements IQuotationService {
    private final QuotationOrderRepository quotationOrderRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final BaseProductRepository baseProductRepository;

    @Override
    public void requestAQuotation(QuotationOrder quotationOrder) throws DuplicationException, NotFoundException, BusinessException {
        this.checkIfAllItemsExists(new ArrayList<>(quotationOrder.getItems()));
        this.checkDuplicationsAndQuantity(new ArrayList<>(quotationOrder.getItems()));
        List<QuotationItem> items = new ArrayList<>(quotationOrder.getItems());
        quotationOrder.setItems(null);
        quotationOrder.setDeleted(false);
        quotationOrder.setUser(FuncUtils.getLoogedUser());
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

    @Override
    public byte[] getQuotation(Integer quotationRequestId) throws Exception {
        final String reportName = "QuotationReport";
        UUID userId = FuncUtils.getLoogedUser().getId();
        Optional<QuotationReport> optionalQuotationReport = this.quotationOrderRepository.findQuotationReport(quotationRequestId, userId);
        if (optionalQuotationReport.isEmpty())
            throw new NotFoundException("Could not find quotation with code: " + quotationRequestId);
        QuotationReport quotationReport =  optionalQuotationReport.get();
        List<QuotationItemReport> quotationItems = this.quotationItemRepository.findItemsReportByQuotationId(quotationRequestId);
        if (quotationItems == null || quotationItems.isEmpty())
            throw new NotFoundException("There ways an error occurred " +
                    "while finding quotation items for Quotation with code: " + quotationRequestId);
        quotationReport.setItems(quotationItems);
        List<QuotationReport> list = new ArrayList<>(List.of(quotationReport));
        Map<String, Object> params = new HashMap<>();
        params.put("NUIT", Supplier.NUIT.getValue());
        params.put("EMAIL", Supplier.EMAIL.getValue());
        params.put("CONTACT", Supplier.CONTACT.getValue());
        params.put("ADDRESS", Supplier.ADDRESS.getValue());
        params.put("orderItems", quotationReport.getItems());
        return Jasper.generatePDF(reportName, params, list);
    }

    private void checkDuplicationsAndQuantity(List<QuotationItem> items) throws DuplicationException, BusinessException {
        Map<Integer, BaseProduct> productMap = new HashMap<>();
        for (QuotationItem item : items) {
            Integer productId = item.getBaseProduct().getId();
            if (productMap.containsKey(productId)) {
                throw new DuplicationException("The item with the code " + productId + " is duplicated!");
            }else if(Objects.equals(item.getQuantity(), 0)){
                throw new BusinessException("The quantity of item with code " + productId
                        +" must be at least one (1) for the quote to be generated.");
            }
            productMap.put(productId, item.getBaseProduct());
        }
    }

    private void checkIfAllItemsExists(List<QuotationItem> items) throws NotFoundException {
        Optional<BaseProduct> baseProduct;
        for (QuotationItem item : items) {
            Integer baseProductId = item.getBaseProduct().getId();
            baseProduct = this.baseProductRepository.findById(baseProductId);
            if (baseProduct.isEmpty())
                throw new NotFoundException("Could not find item in index: " + items.indexOf(item)
                        + " with code: " + baseProductId);
        }
    }
}
