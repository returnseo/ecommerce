package com.study.ecommerce.infra.payment.external.toss;

import lombok.Builder;

@Builder
public record TossPaymentRequest(
        String orderId,
        int amount,
        String orderName,
        String customerEmail,
        String customerName,
        String successUrl,
        String failUrl
) {
}
