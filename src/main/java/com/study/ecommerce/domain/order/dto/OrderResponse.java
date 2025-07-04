package com.study.ecommerce.domain.order.dto;

import com.study.ecommerce.domain.order.entity.Order.OrderStatus;

public record OrderResponse(
        Long id,
        OrderStatus status,
        Long totalAmount
) {
}
