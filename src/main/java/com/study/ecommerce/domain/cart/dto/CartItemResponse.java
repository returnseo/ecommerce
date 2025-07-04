package com.study.ecommerce.domain.cart.dto;

public record CartItemResponse(
        Long id,
        Long productId,
        String productName,
        Long price,
        Integer quantity,
        Long totalPrice
) {
}
