package com.study.ecommerce.infra.payment.adapter;

import com.study.ecommerce.infra.payment.dto.PaymentGatewayRequest;
import com.study.ecommerce.infra.payment.dto.PaymentGatewayResponse;
import com.study.ecommerce.infra.payment.external.toss.TosePaymentApi;
import com.study.ecommerce.infra.payment.external.toss.TossCancelRequest;
import com.study.ecommerce.infra.payment.external.toss.TossPaymentRequest;
import com.study.ecommerce.infra.payment.external.toss.TossPaymentResponse;
import com.study.ecommerce.infra.payment.gateway.PaymentGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class TossPaymentAdapter implements PaymentGateway {
    private final TosePaymentApi tosePaymentApi;

    @Override
    public PaymentGatewayResponse processPayment(PaymentGatewayRequest request) {
        log.info("토스페이먼츠 어댑터 - 결제 요청 변환 및 처리");

        // 1. 우리 시스템의 요청을 토스 API 형태로 변환
        TossPaymentRequest tossPaymentRequest = convertToTossRequest(request);

        // 2. 토스 API 호출
        TossPaymentResponse tossPaymentResponse = tosePaymentApi.requestPayment(tossPaymentRequest);

        // 3. 토스 응답을 우리 시스템 형태로 변환
        return convertToGatewayResponse(tossPaymentResponse);
    }

    @Override
    public PaymentGatewayResponse getPaymentStatus(String transactionId) {
        TossPaymentResponse payment = tosePaymentApi.getPayment(transactionId);
        return convertToGatewayResponse(payment);
    }

    @Override
    public PaymentGatewayResponse cancelPayment(String transactionId, int cancelAmount) {
        TossCancelRequest cancelRequest = TossCancelRequest.builder()
                .cancelAmount(cancelAmount)
                .cancelReason("고객 요청")
                .build();

        TossPaymentResponse tossPaymentResponse = tosePaymentApi.cancelPayment(transactionId, cancelRequest);
        return convertToGatewayResponse(tossPaymentResponse);
    }

    @Override
    public String getGatewayName() {
        return "TOSS_PAYMENTS";
    }

    /**
     *  공통 요청을 토스 API 요청으로 변환
     */
    private TossPaymentRequest convertToTossRequest(PaymentGatewayRequest request) {
        return TossPaymentRequest.builder()
                .orderId(request.orderId())
                .amount(request.amount())
                .orderName("주문명-" + request.orderId())
                .customerEmail(request.customerEmail())
                .customerName(request.customerName())
                .successUrl(request.returnUrl())
                .failUrl(request.cancelUrl())
                .build();
    }

    /**
     * 토스 API 응답을 공통 응답으로 변환
     */
    private PaymentGatewayResponse convertToGatewayResponse(TossPaymentResponse response) {
        String status = convertTossStatus(response.status());

        return PaymentGatewayResponse.builder()
                .success(response.success() && "SUCCESS".equals(status))
                .transactionId(response.paymentKey())
                .status(status)
                .message(response.success() ? "결제 성공" : response.errorMessage())
                .paidAmount(response.amount())
                .errorCode(response.errorCode())
                .build();
    }

    /**
     *  토스 상태를 공통 상태로 변환
     */
    private String convertTossStatus(String tossStatus) {
        if (tossStatus == null) return "PENDING";

        return switch (tossStatus) {
            case "DONE" -> "SUCCESS";
            case "CANCELLED", "PARTIAL_CANCELLED" -> "CANCELLED";
            case "ABORTED", "EXPIRED" -> "FAILED";
            case "READY", "IN_PROGRESS", "WAITING_FOR_DEPOSIT" -> "PENDING";
            default -> "PENDING";
        };
    }
}
