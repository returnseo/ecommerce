package com.study.ecommerce.domain.payment.factory;

import com.study.ecommerce.domain.payment.processor.PaymentProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class PaymentProcessorFactory {
    private final List<PaymentProcessor> paymentProcessors;

    /**
     *  결제 방식에 맞는 PaymentProcessor를 반환
     */
    public PaymentProcessor createPaymentProcessor(String paymentMethod) {
        return paymentProcessors.stream()
                .filter(processor -> processor.supports(paymentMethod))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException(
                        "지원하지 않는 결제 방식입니다: " + paymentMethod
                ));
    }

    /**
     *  사용가능한 모든 결제 처리기 목록 반환
     */
    public List<PaymentProcessor> getAllProcessors() {
        return paymentProcessors;
    }
}
