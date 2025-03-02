package com.itelectric.backend.v1.utils;

import com.itelectric.backend.v1.api.dto.response.ProductResponse;
import com.itelectric.backend.v1.api.dto.response.QuotationItemResponse;
import com.itelectric.backend.v1.api.dto.response.QuotationResponse;
import com.itelectric.backend.v1.domain.entity.BaseProduct;
import com.itelectric.backend.v1.domain.entity.Product;
import com.itelectric.backend.v1.domain.entity.Quotation;
import com.itelectric.backend.v1.domain.entity.QuotationItem;
import com.itelectric.backend.v1.domain.enums.GeralBigDecimalEnuns;
import com.itelectric.backend.v1.domain.report.QuotationItemReport;
import com.itelectric.backend.v1.domain.report.QuotationReport;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

public class Tax {
    //    Calculate the final price without IVA informed
    public static BigDecimal calculatePriceWithoutIVAInformed(BigDecimal price) {
        return price.multiply(GeralBigDecimalEnuns.IVA_RATE.getValue()).setScale(2, RoundingMode.HALF_UP);
    }

    public static BigDecimal calculateIVAFromPrice(BigDecimal price) {
        BigDecimal priceWithNoIVA = price.divide(GeralBigDecimalEnuns.IVA_RATE.getValue());
        return price.subtract(priceWithNoIVA).setScale(2, RoundingMode.HALF_UP);
    }

    public static QuotationResponse calculateIVAAndMapToQuotationResponse(Quotation entity) {
        List<QuotationItemResponse> itemResponses =
                Tax.calculateIVAAndMapToListQuotationItemResponse(new ArrayList<>(entity.getItems()));

        BigDecimal[] totals = itemResponses.parallelStream()
                .map(item -> new BigDecimal[]{item.getTotalIVA(), item.getTotalPrice(), item.getTotalPriceWithIVA()})
                .reduce(new BigDecimal[]{BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO},
                        (acc, values) -> new BigDecimal[]{
                                acc[0].add(values[0]),
                                acc[1].add(values[1]),
                                acc[2].add(values[2])
                        });

        return QuotationResponse.builder()
                .name(entity.getUser().getName())
                .nuit(entity.getUser().getNuit())
                .cellPhone(entity.getUser().getContact().getPhone())
                .email(entity.getUser().getContact().getEmail())
                .address(FuncUtils.getFormatedAddress(entity.getUser().getAddress()))
                .quotationId(entity.getId())
                .issueDate(entity.getCreatedDate())
                .items(itemResponses)
                .totalIVA(scaleValue(totals[0]))
                .totalPrice(scaleValue(totals[1]))
                .totalPriceWithIVA(scaleValue(totals[2]))
                .build();
    }

    public static QuotationItemResponse calculateIVAAndMapToQuotationItemResponse(QuotationItem entity) {
        BaseProduct product = entity.getBaseProduct();
        int quantity = entity.getQuantity();
        BigDecimal basePrice = product.getPrice();

        BigDecimal IVA = product.isHasIVA() ?
                Tax.calculateIVAFromPrice(basePrice) :
                Tax.calculatePriceWithoutIVAInformed(basePrice);

        BigDecimal unitPriceWithoutIVA = product.isHasIVA() ? basePrice.subtract(IVA) : basePrice;

        BigDecimal totalIVA = IVA.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPrice = unitPriceWithoutIVA.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);
        BigDecimal totalPriceWithIVA = basePrice.multiply(BigDecimal.valueOf(quantity)).setScale(2, RoundingMode.HALF_UP);

        return QuotationItemResponse.builder()
                .baseProductId(product.getId())
                .baseProductName(product.getName())
                .baseProductType(product.getType().name())
                .baseProductUnitPrice(unitPriceWithoutIVA)
                .totalPrice(totalPrice)
                .totalPriceWithIVA(totalPriceWithIVA)
                .baseProductDescription(product.getDescription())
                .IVA(IVA)
                .totalIVA(totalIVA)
                .quantity(quantity)
                .build();
    }

    public static List<QuotationItemResponse> calculateIVAAndMapToListQuotationItemResponse(List<QuotationItem> items) {
        return items
                .stream()
                .map(Tax::calculateIVAAndMapToQuotationItemResponse)
                .toList();
    }

    public static QuotationReport calculateIVAAndMapToQuotationReport(QuotationReport entity) {
        List<QuotationItemReport> quotationItems = calculateIVAAndMapToListQuotationItemReport(entity.getItems());

        BigDecimal totalIVA = quotationItems.stream()
                .map(QuotationItemReport::getTotalIVA)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPrice = quotationItems.stream()
                .map(QuotationItemReport::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalPriceWithIVA = quotationItems.stream()
                .map(QuotationItemReport::getTotalPriceWithIVA)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return QuotationReport.builder()
                .name(entity.getName())
                .nuit(entity.getNuit())
                .cellPhone(entity.getCellPhone())
                .email(entity.getEmail())
                .addressEmbedded(entity.getAddressEmbedded())
                .quotationId(entity.getQuotationId())
                .issueDate(entity.getIssueDate())
                .items(quotationItems)
                .totalIVA(scaleValue(totalIVA))
                .totalPrice(scaleValue(totalPrice))
                .totalPriceWithIVA(scaleValue(totalPriceWithIVA))
                .build();
    }

    public static QuotationItemReport calculateIVAAndMapToQuotationItemReport(QuotationItemReport item) {
        BigDecimal basePrice = item.getBaseProductUnitPrice();
        BigDecimal IVA = item.isHasIVA()
                ? Tax.calculateIVAFromPrice(basePrice)
                : Tax.calculatePriceWithoutIVAInformed(basePrice);

        BigDecimal updatedBasePrice = item.isHasIVA() ? basePrice.subtract(IVA) : basePrice;
        BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

        BigDecimal totalIVA = scaleValue(IVA.multiply(quantity));
        BigDecimal totalPrice = scaleValue(updatedBasePrice.multiply(quantity));
        BigDecimal totalPriceWithIVA = scaleValue(updatedBasePrice.add(IVA).multiply(quantity));

        return QuotationItemReport.builder()
                .baseProductId(item.getBaseProductId())
                .baseProductName(item.getBaseProductName())
                .baseProductType(item.getBaseProductType())
                .baseProductUnitPrice(updatedBasePrice)
                .totalPrice(totalPrice)
                .totalPriceWithIVA(totalPriceWithIVA)
                .baseProductDescription(item.getBaseProductDescription())
                .IVA(IVA)
                .totalIVA(totalIVA)
                .quantity(item.getQuantity())
                .build();
    }

    public static List<QuotationItemReport> calculateIVAAndMapToListQuotationItemReport(List<QuotationItemReport> items) {
        return items
                .stream()
                .map(Tax::calculateIVAAndMapToQuotationItemReport)
                .toList();
    }

    public static ProductResponse calculateIVAAndMapToProductEntity(Product entity) {
        BigDecimal IVA;
        if (entity.isHasIVA()) {
            IVA = Tax.calculateIVAFromPrice(entity.getPrice());
            entity.setPrice(entity.getPrice().subtract(IVA));
        } else {
            IVA = Tax.calculatePriceWithoutIVAInformed(entity.getPrice());
        }

        return ProductResponse.builder()
                .id(entity.getId())
                .name(entity.getName())
                .type(entity.getType().name())
                .price(scaleValue(entity.getPrice()))
                .description(entity.getDescription())
                .IVA(scaleValue(IVA))
                .stockQuantity(entity.getStockQuantity())
                .build();
    }

    public static List<ProductResponse> calculateIVAAndMapToListProductEntity(List<Product> products) {
        return products
                .stream()
                .map(Tax::calculateIVAAndMapToProductEntity)
                .toList();
    }

    public static BigDecimal scaleValue(BigDecimal value) {
        return value.setScale(2, RoundingMode.HALF_UP);
    }
}
