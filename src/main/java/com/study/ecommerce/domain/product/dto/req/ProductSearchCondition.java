package com.study.ecommerce.domain.product.dto.req;

public record ProductSearchCondition(
        String keyword,
        Long categoryId,
        Long minPrice,
        Long maxPrice,
        Long sellerId
) {
}
