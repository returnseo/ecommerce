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
        if (!isSupportedProvider(request.simplePayProvider())) {
            return PaymentResult.builder()
                    .success(false)
                    .message("지원하지 않는 간편결제 서비스 입니다.")
                    .paymentMethod("SIMPLE_PAY")
                    .build();
        }

        if (request.amount() > MAX_AMOUNT) {
            return PaymentResult.builder()
                    .success(false)
                    .message("간편결제 한도를 초과했습니다.")
                    .paymentMethod("SIMPLE_PAY")
                    .build();
        }

        // 실제 간편결제 처리
        String transactionId = request.simplePayProvider() + "-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
        int feeAmount = calculateFee(request.amount());

        return PaymentResult.builder()
                .success(true)
                .transactionId(transactionId)
                .message(request.simplePayProvider() + " 결제가 완료되었습니다.")
                .paidAmount(request.amount())
                .feeAmount(feeAmount)
                .paymentMethod("SIMPLE_PAY")
                .build();
    }

    @Override
    public int calculateFee(int amount) {
        return (int) (amount * FEE_RATE);
    }

    @Override
    public boolean supports(String paymentMethod) {
        return "SIMPLE_PAY".equals(paymentMethod);
    }

    @Override
    public int getMaxAmount() {
        return MAX_AMOUNT;
    }

    // isSupportedProvider(String provider)
    private boolean isSupportedProvider(String provider) {
        return provider != null && SUPPORTED_PROVIDERS.contains(provider);
    }
}
