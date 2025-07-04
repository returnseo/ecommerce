package com.study.ecommerce.infra.payment.dto;

import lombok.Builder;

@Builder
public record PaymentGatewayRequest(
        String merchantId,
        String orderId,
        int amount,
        String currency,
        String paymentMethod,
        String returnUrl,
        String cancelUrl,
        String customerEmail,
        String customerName
) {
}
