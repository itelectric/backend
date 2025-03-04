package com.itelectric.backend.v1.service.contract;

import com.itelectric.backend.v1.domain.entity.QuotationOrder;
import com.itelectric.backend.v1.domain.exception.BusinessException;
import com.itelectric.backend.v1.domain.exception.DuplicationException;
import com.itelectric.backend.v1.domain.exception.NotFoundException;

import java.io.IOException;

public interface IQuotationService {
    void requestAQuotation(QuotationOrder quotationOrder) throws DuplicationException, NotFoundException, BusinessException;

    byte[] getQuotation(Integer quotationRequestId) throws Exception;
}