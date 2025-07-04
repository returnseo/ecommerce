package com.study.ecommerce.domain.payment.dto;

import lombok.Builder;

@Builder
public record PaymentResult(
        boolean success,
        String transactionId,
        String message,
        int paidAmount,
        int feeAmount,
        String paymentMethod
) {
}
