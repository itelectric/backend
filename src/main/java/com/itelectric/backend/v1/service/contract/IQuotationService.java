package com.itelectric.backend.v1.service.contract;

import com.itelectric.backend.v1.domain.entity.Quotation;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.DuplicationException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;

public interface IQuotationService {
    void requestQuotation(Quotation quotation) throws DuplicationException, NotFoundException, BusinessException;

    byte[] generateQuotationReport(Integer quotationRequestId) throws Exception;
}