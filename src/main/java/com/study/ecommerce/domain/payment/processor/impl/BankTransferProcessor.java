package com.study.ecommerce.domain.payment.processor.impl;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;
import com.study.ecommerce.domain.payment.processor.PaymentProcessor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class BankTransferProcessor implements PaymentProcessor {
    private static final int MAX_AMOUNT = 10_000_000;
    private static final int FIXED_FEE = 500;

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

    // isValidAccount -> 10자리이상, 14자리 이하
}
