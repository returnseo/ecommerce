package com.study.ecommerce.infra.payment.gateway;

import com.study.ecommerce.infra.payment.dto.PaymentGatewayRequest;
import com.study.ecommerce.infra.payment.dto.PaymentGatewayResponse;

/**
 *  결제 게이트웨이 공통 인터페이스
 */
public interface PaymentGateway {

    PaymentGatewayResponse processPayment(PaymentGatewayRequest request);

    PaymentGatewayResponse getPaymentStatus(String transactionId);

    PaymentGatewayResponse cancelPayment(String transactionId, int cancelAmount);

    String getGatewayName();
}
