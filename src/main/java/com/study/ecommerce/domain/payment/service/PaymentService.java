package com.study.ecommerce.domain.payment.service;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;
import com.study.ecommerce.domain.payment.factory.PaymentProcessorFactory;
import com.study.ecommerce.domain.payment.processor.PaymentProcessor;
import com.study.ecommerce.infra.payment.dto.PaymentGatewayRequest;
import com.study.ecommerce.infra.payment.dto.PaymentGatewayResponse;
import com.study.ecommerce.infra.payment.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class PaymentService {
    private final PaymentProcessorFactory paymentProcessorFactory;
    private final List<PaymentGateway> paymentGateways;

    /**
     *  Factory Pattern 을 사용해서 결제 처리
     */
    public PaymentResult processPaymentWithFactory(PaymentRequest request) {
        try {
            PaymentProcessor paymentProcessor = paymentProcessorFactory.createPaymentProcessor(request.paymentMethod());

            // 결제 처리
            return paymentProcessor.process(request);
        } catch (IllegalArgumentException e) {
            return PaymentResult.builder()
                    .success(false)
                    .message("지원하지 않는 결제 방식입니다: " + request.paymentMethod())
                    .paymentMethod(request.paymentMethod())
                    .build();
        }
    }

    /**
     * adapter
     * @param gatewayName
     * @param request
     * @return
     */
    public PaymentGatewayResponse processPaymentWithAdapter(String gatewayName, PaymentGatewayRequest request) {
        PaymentGateway gateway = findPaymentGateway(gatewayName);
        if (gateway == null) {
            return PaymentGatewayResponse.builder()
                    .success(false)
                    .message("지원하지 않는 결제 게이트웨이입니다: " + gatewayName)
                    .status("FAILED")
                    .build();
        }

        return gateway.processPayment(request);
    }

    /**
     * 사용가능한 모든 결제방식을 조회
     */
    public List<String> getAvailablePaymentMethods() {
        return paymentProcessorFactory.getAllProcessors()
                .stream()
                .map(processor -> {
                    if (processor.supports("CARD")) return "CARD";
                    if (processor.supports("BANK_TRANSFER")) return "BANK_TRANSFER";
                    if (processor.supports("SIMPLE_PAY")) return "SIMPLE_PAY";
                    return "UNKNOWN";
                })
                .distinct()
                .toList();
    }

    /**
     * 사용 가능한 모든 결제 게이트웨이 조회
     */
    public List<String> getAvailablePaymentGateways() {
        return paymentGateways.stream()
                .map(PaymentGateway::getGatewayName)
                .toList();
    }

    private PaymentGateway findPaymentGateway(String gatewayName) {
        return paymentGateways.stream()
                .filter(gateway -> gateway.getGatewayName().equals(gatewayName))
                .findFirst()
                .orElse(null);
    }
}
