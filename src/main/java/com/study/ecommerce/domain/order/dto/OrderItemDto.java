package com.study.ecommerce.domain.order.dto;

public record OrderItemDto(
        Long productId,
        String productName,
        Integer quantity,
        Long price
) {
}
