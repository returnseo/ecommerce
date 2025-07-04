package com.study.ecommerce.domain.payment.enums;

public enum PaymentMethodType {
    CARD("카드결제"),
    BANK_TRANSFER("계좌이체"),
    SIMPLE_PAY("간편결제"),
    POINT("포인트 결제");

    private final String description;

    PaymentMethodType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
