package com.study.ecommerce.domain.payment.processor;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;

/**
 *  결제 처리기 인터페이스
 */
public interface PaymentProcessor {

    /**
     *  결제 처리
     */
    PaymentResult process(PaymentRequest request);

    /**
     *  결제 수수료 계산
     *
     */
    int calculateFee(int amount);

    /**
     * 지원하는 결제 방식 확인
     */
    boolean supports(String paymentMethod);

    /**
     * 최대 결제 가능 금액
     */
    int getMaxAmount();

}
