package com.study.ecommerce.domain.payment.processor.impl;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;
import com.study.ecommerce.domain.payment.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CardPaymentProcessor implements PaymentProcessor {
    private static final int MAX_AMOUNT = 5_000_000;
    private static final double FEE_RATE = 0.025; // 2.5%

    @Override
    public PaymentResult process(PaymentRequest request) {
        log.info("카드 결제 처리 시작 - 주문ID: {}, 금액: {}", request.orderId(), request.amount());

        // 카드 유효성 검증
        if (!isValidCard(request.cardNumber())) {
            return PaymentResult.builder()
                    .success(false)
                    .message("유효하지 않은 카드입니다.")
                    .paymentMethod("CARD")
                    .build();
        }

        // 결제 한도를 확인
        if (request.amount() > MAX_AMOUNT) {
            return PaymentResult.builder()
                    .success(false)
                    .message("결제 한도를 초과했습니다.")
                    .paymentMethod("CARD")
                    .build();
        }

        String transactionId = UUID.randomUUID().toString();
        int feeAmount = calculateFee(request.amount());

        log.info("카드 결제 완료 - 거래ID: {}, 수수료: {}", transactionId, feeAmount);

        return PaymentResult.builder()
                .success(true)
                .transactionId(transactionId)
                .message("카드 결제가 완료되었습니다.")
                .paidAmount(request.amount())
                .feeAmount(feeAmount)
                .paymentMethod("CARD")
                .build();
    }

    @Override
    public int calculateFee(int amount) {
        return (int) (amount * FEE_RATE);
    }

    @Override
    public boolean supports(String paymentMethod) {
        return "CARD".equals(paymentMethod);
    }

    @Override
    public int getMaxAmount() {
        return MAX_AMOUNT;
    }

    private boolean isValidCard(String cardNumber) {
        return cardNumber != null && cardNumber.length() >= 13 && cardNumber.length() <= 19;
    }
}
