package com.study.ecommerce.domain.payment.processor.impl;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;
import com.study.ecommerce.domain.payment.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Slf4j
@Component
public class SimplePayProcessor implements PaymentProcessor {
    private static final int MAX_AMOUNT = 3_000_000;
    private static final double FEE_RATE = 0.015;
    private static final List<String> SUPPORTED_PROVIDERS = Arrays.asList(
            "KAKAO_PAY", "NAVER_PAY", "PAYCO", "TOSS_PAY", "SAMSUNG_PAY"
    );

    @Override
    public PaymentResult process(PaymentRequest request) {

        // 결제 유효성 검증
        if (!isSupportedProvider(request.cardNumber())) {
            return PaymentResult.builder()
                    .success(false)
                    .message("사용 불가능한 간편 결제 입니다.")
                    .paymentMethod("SIMPLE_PAYMENT")
                    .build();
        }

        // 결제 한도를 확인
        if (request.amount() > MAX_AMOUNT) {
            return PaymentResult.builder()
                    .success(false)
                    .message("결제 한도를 초과했습니다.")
                    .paymentMethod("SIMPLE_PAYMENT")
                    .build();
        }

        String transactionId = UUID.randomUUID().toString();
        int feeAmount = calculateFee(request.amount());

        return PaymentResult.builder()
                .success(true)
                .transactionId(transactionId)
                .message("간편 결제가 완료되었습니다.")
                .paidAmount(request.amount())
                .feeAmount(feeAmount)
                .paymentMethod("SIMPLE_PAYMENT")
                .build();
    }

    @Override
    public int calculateFee(int amount) {
        return (int) (amount * FEE_RATE);
    }

    @Override
    public boolean supports(String paymentMethod) {
        return "SIMPLE_PAYMENT".equals(paymentMethod);
    }

    @Override
    public int getMaxAmount() {
        return MAX_AMOUNT;
    }

    // isSupportedProvider(String provider)
    private boolean isSupportedProvider(String payment) {
        return payment != null && SUPPORTED_PROVIDERS.contains(payment);
    }
}
