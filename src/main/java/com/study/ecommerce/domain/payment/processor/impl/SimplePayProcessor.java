package com.study.ecommerce.domain.payment.processor.impl;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;
import com.study.ecommerce.domain.payment.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

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
        return null;
    }

    @Override
    public int calculateFee(int amount) {
        return 0;
    }

    @Override
    public boolean supports(String paymentMethod) {
        return false;
    }

    @Override
    public int getMaxAmount() {
        return 0;
    }

    // isSupportedProvider(String provider)
}
