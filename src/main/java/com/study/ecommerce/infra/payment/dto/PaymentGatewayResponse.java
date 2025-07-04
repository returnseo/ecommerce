package com.study.ecommerce.infra.payment.dto;

import lombok.Builder;

@Builder
public record PaymentGatewayResponse(
        boolean success,
        String transactionId,
        String status,
        String message,
        int paidAmount,
        String paymentUrl,
        String errorCode
) {
}
