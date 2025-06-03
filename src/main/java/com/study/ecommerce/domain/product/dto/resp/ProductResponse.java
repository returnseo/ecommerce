package com.study.ecommerce.domain.product.dto.resp;

import com.study.ecommerce.domain.product.entity.Product.ProductStatus;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Long price,
        Integer stockQuantity,
        ProductStatus status,
        String categoryName
) {
}
