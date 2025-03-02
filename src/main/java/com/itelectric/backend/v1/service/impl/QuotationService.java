package com.itelectric.backend.v1.service.impl;

import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.Quotation;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.domain.enums.Supplier;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.DuplicationException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;
import com.itelectric.backend.v1.domain.report.QuotationItemReport;
import com.itelectric.backend.v1.domain.report.QuotationReport;
import com.itelectric.backend.v1.repository.BaseProductRepository;
import com.itelectric.backend.v1.repository.QuotationItemRepository;
import com.itelectric.backend.v1.repository.QuotationRepository;
import com.itelectric.backend.v1.service.contract.IQuotationService;
import com.itelectric.backend.v1.utils.FuncUtils;
import com.itelectric.backend.v1.utils.Jasper;
import com.itelectric.backend.v1.utils.Tax;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@Transactional
@RequiredArgsConstructor
public class QuotationService implements IQuotationService {
    private final QuotationRepository quotationRepository;
    private final QuotationItemRepository quotationItemRepository;
    private final BaseProductRepository baseProductRepository;

    @Override
    public void requestQuotation(Quotation quotation) throws DuplicationException, NotFoundException, BusinessException {
        this.checkIfAllItemsExists(new ArrayList<>(quotation.getItems()));
        this.checkDuplicationsAndQuantity(new ArrayList<>(quotation.getItems()));
        List<QuotationItem> items = new ArrayList<>(quotation.getItems());
        quotation.setItems(null);
        quotation.setDeleted(false);
        quotation.setUser(FuncUtils.getLoogedUser());
        quotation = (Quotation) FuncUtils.setAuditFields(quotation);
        Quotation finalQuotation = quotationRepository.save(quotation);

        items.forEach(item -> {
            item.setDeleted(false);
            item.setQuotation(finalQuotation);
            item = (QuotationItem) FuncUtils.setAuditFields(item);
            quotationItemRepository.save(item);
        });

        finalQuotation.setItems(new HashSet<>(items));
        quotationRepository.save(finalQuotation);
    }

    @Override
    public byte[] generateQuotationReport(Integer quotationRequestId) throws Exception {
        final String reportName = "QuotationReport";
        UUID userId = FuncUtils.getLoogedUser().getId();
        Optional<QuotationReport> optionalQuotationReport = this.quotationRepository.findQuotationReport(quotationRequestId, userId);
        if (optionalQuotationReport.isEmpty())
            throw new NotFoundException("Could not find quotation with code: " + quotationRequestId);
        QuotationReport quotationReport = optionalQuotationReport.get();
        List<QuotationItemReport> quotationItems = this.quotationItemRepository.findItemsReportByQuotationId(quotationRequestId);
        if (quotationItems == null || quotationItems.isEmpty())
            throw new NotFoundException("There ways an error occurred " +
                    "while finding quotation items for Quotation with code: " + quotationRequestId);
        quotationReport.setItems(quotationItems);
        quotationReport = Tax.calculateIVAAndMapToQuotationReport(quotationReport);
        List<QuotationReport> list = new ArrayList<>(List.of(quotationReport));
        Map<String, Object> params = new HashMap<>();
        params.put("NUIT", Supplier.NUIT.getValue());
        params.put("EMAIL", Supplier.EMAIL.getValue());
        params.put("CONTACT", Supplier.CONTACT.getValue());
        params.put("ADDRESS", Supplier.ADDRESS.getValue());
        params.put("orderItems", quotationReport.getItems());
        return Jasper.generatePDF(reportName, params, list);
    }

    public Page<Quotation> readAll(int pageNo, int pageSize) {
        UUID userId = FuncUtils.getLoogedUser().getId();
        Pageable pageable = PageRequest.of(pageNo, pageSize);
        return this.quotationRepository.findAll(userId, pageable);
    }

    private void checkDuplicationsAndQuantity(List<QuotationItem> items) throws DuplicationException, BusinessException {
        Map<Integer, BaseProduct> productMap = new HashMap<>();
        for (QuotationItem item : items) {
            Integer productId = item.getBaseProduct().getId();
            if (productMap.containsKey(productId)) {
                throw new DuplicationException("The item with the code " + productId + " is duplicated!");
            } else if (Objects.equals(item.getQuantity(), 0)) {
                throw new BusinessException("The quantity of item with code " + productId
                        + " must be at least one (1) for the quote to be generated.");
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
