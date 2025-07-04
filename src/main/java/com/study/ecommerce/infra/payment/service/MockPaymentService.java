package com.study.ecommerce.infra.payment.service;

import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.payment.entity.Payment;
import com.study.ecommerce.domain.payment.entity.Payment.PaymentMethod;
import com.study.ecommerce.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

import static com.study.ecommerce.domain.payment.entity.Payment.PaymentStatus.CANCELED;
import static com.study.ecommerce.domain.payment.entity.Payment.PaymentStatus.PENDING;

@Service
@RequiredArgsConstructor
public class MockPaymentService {

    private final PaymentRepository paymentRepository;

    @Transactional
    public Payment processPayment(Order order, PaymentMethod paymentMethod) {
        // 결제 이력 생성
        Payment payment = Payment.builder()
                .orderId(order.getId())
                .paymentMethod(paymentMethod)
                .status(PENDING)
                .amount(order.getTotalAmount().longValue())
                .build();

        paymentRepository.save(payment);

        // 실제 PG사 연동 대신 가상의 결제처리 (항상 성공으로 지금은 판단)
        String mockTransactionId = UUID.randomUUID().toString();
        simulatePaymentGatewayDelay();

        payment.complete();
        return payment;

    }

    @Transactional
    public Payment cancelPayment(Payment payment) {
        // 실제 PG 사 연동을 해야함 하지만 우리는 상태만 변경
        simulatePaymentGatewayDelay();
        payment.updateStatus(CANCELED);
        return payment;
    }

    private void simulatePaymentGatewayDelay() {
        try {
            Thread.sleep(300); // 300ms 지연
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
