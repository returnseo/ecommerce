package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Slf4j
@Component
public class PaymentValidationHandler extends OrderValidationHandler{
    private static final BigDecimal MINIMUM_ORDER_AMOUNT = new BigDecimal("1000");
    private static final BigDecimal MAXIMUM_ORDER_AMOUNT = new BigDecimal("1000000");
    @Override
    protected void doValidate(OrderCreateRequest request) {
        // 결제방법 검증
        // 지원하는 결제 방법인지 검증

        // 주문 총액 계산

        // 최소 주문 금액 검증

        // 최대 주문 금액 검증

        // 결제 방법별 추가검증
    }

    @Override
    protected String getHandlerName() {
        return "";
    }

    private void validatePaymentMethodSpecific(OrderCreateRequest request, BigDecimal totalAmount) {
        String paymentMethod = request.paymentMethod();

        switch (paymentMethod) {
            case "CARD" -> validateCardPayment(request, totalAmount);
            case "BANK_TRANSFER" -> validateBankTransferPayment(request, totalAmount);
            case "VIRTUAL_ACCOUNT" -> validateVirtualAccountPayment(request, totalAmount);
            case "POINT" -> validatePointPayment(request, totalAmount);
        }
    }

    private void validatePointPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        // 포인트 compareTo 5만포인트
    }

    private void validateVirtualAccountPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        // 로그
    }

    private void validateCardPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        // 최소금액 검증
    }

    private void validateBankTransferPayment(OrderCreateRequest request, BigDecimal totalAmount) {
        //  영업시간에만 가능하다
    }
}
