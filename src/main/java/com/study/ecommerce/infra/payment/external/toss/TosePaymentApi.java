package com.study.ecommerce.infra.payment.external.toss;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


// 실제 toss 에서 제공하는 api
@Slf4j
@Component
public class TosePaymentApi {

    public TossPaymentResponse requestPayment(TossPaymentRequest request) {
        log.info("토스페이먼츠 API 호출: {}", request);

        return TossPaymentResponse.builder()
                .success(true)
                .paymentKey("toss_" + System.currentTimeMillis())
                .orderId(request.orderId())
                .amount(request.amount())
                .status("DONE")
                .approvedAt(LocalDateTime.now().toString())
                .build();
    }

    public TossPaymentResponse getPayment(String paymentKey) {
        log.info("토스페이먼츠 결제 조회: {}", paymentKey);

        return TossPaymentResponse.builder()
                .success(true)
                .paymentKey(paymentKey)
                .status("DONE")
                .build();
    }

    public TossPaymentResponse cancelPayment(String paymentKey, TossCancelRequest request) {
        log.info("토스페이먼츠 결제 취소: {} - {}", paymentKey, request);

        return TossPaymentResponse.builder()
                .success(true)
                .paymentKey(paymentKey)
                .status("CANCELLED")
                .build();
    }
}
