package com.study.ecommerce.domain.cart.dto;

import java.util.List;

public record CartResponse(
        Long id,
        Long totalPrice,
        List<CartItemResponse> items
) {
}
