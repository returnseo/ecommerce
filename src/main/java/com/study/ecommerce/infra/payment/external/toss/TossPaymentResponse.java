package com.study.ecommerce.infra.payment.external.toss;

import lombok.Builder;

@Builder
public record TossPaymentResponse(
        boolean success,
        String paymentKey,
        String orderId,
        int amount,
        String status,
        String approvedAt,
        String method,
        String errorCode,
        String errorMessage
) {
}
