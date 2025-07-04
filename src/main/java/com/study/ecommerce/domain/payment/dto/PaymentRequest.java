package com.study.ecommerce.domain.payment.dto;

public record PaymentRequest(
        String orderId,
        int amount,
        String paymentMethod,
        String cardNumber,
        String accountNumber,
        String simplePayProvider,
        String customerId
) {
}
