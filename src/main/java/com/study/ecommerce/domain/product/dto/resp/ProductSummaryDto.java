package com.study.ecommerce.domain.product.dto.resp;

import com.study.ecommerce.domain.product.entity.Product.ProductStatus;

public record ProductSummaryDto(
        Long id,
        String name,
        Long price,
        Integer stockQuantity,
        String categoryName,
        ProductStatus status
) {
}
