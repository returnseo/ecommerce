package com.study.ecommerce.domain.order.dto;

import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.order.entity.Order.OrderStatus;

import java.time.LocalDateTime;
import java.util.List;

public record OrderDetailResponse(
        Long id,
        Long memberId,
        String memberName,
        OrderStatus status,
        LocalDateTime orderDate,
        Long totalAmount,
        List<OrderItemDto> orderItems
) {
}
