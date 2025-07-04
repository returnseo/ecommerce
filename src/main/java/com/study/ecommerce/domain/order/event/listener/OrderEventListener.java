package com.study.ecommerce.domain.order.event.listener;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.order.event.OrderCancelledEvent;
import com.study.ecommerce.domain.order.event.OrderCreatedEvent;
import com.study.ecommerce.domain.order.event.OrderPaidEvent;
import com.study.ecommerce.domain.order.event.OrderShippedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

/**
 * 주문 이벤트 리스너 - 이메일 알림담당
 */
@Slf4j
@Component
public class OrderEventListener {

    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {
        log.info("주문 생성 이벤트 수신: {}", event);

        sendOrderConfirmationEmail(event.getOrder());

        notifyAdminNewOrder(event.getOrder());
    }

    @EventListener
    @Async
    public void handleOrderPaid(OrderPaidEvent event) {
        log.info("주문 결제 완료 이벤트 수신: {}", event);
        sendPaymentConfirmationEmail(event.getOrder(), event.getPaymentMethod());
        sendPaymentConfirmationSms(event.getOrder());
    }

    @EventListener
    @Async
    public void handleOrderShipped(OrderShippedEvent event) {
        log.info("주문 배송 시작 이벤트 수신: {}", event);
        sendShippingNotificationEmail(event.getOrder(), event.getTrackingNumber(), event.getShippingCompany());
        sendTrackingSms(event.getOrder(), event.getTrackingNumber());
    }

    @EventListener
    @Async
    public void handleOrderCancelled(OrderCancelledEvent event) {
        log.info("주문 취소 이벤트 수신: {}", event);
        sendCancellationEmail(event.getOrder(), event.getCancelReason());
        sendRefundProcessEmail(event.getOrder());
    }

    private void sendOrderConfirmationEmail(Order order) {
        log.info("주문 확인 이메일 발송: 주문 번호 {}", order.getId());
        //  실제 이메일 발송 로직
    }

    private void notifyAdminNewOrder(Order order) {
        log.info("관리자 새 주문 알림: 주문번호 {}", order.getId());
    }

    private void sendPaymentConfirmationEmail(Order order, String paymentMethod) {
        log.info("결제 확인 이메일 발송: 주문 번호 {}, 결제 방법 {}", order.getId(), paymentMethod);
    }

    private void sendPaymentConfirmationSms(Order order) {
        log.info("결제 확인 SMS 발송: 주문 번호 {}", order.getId());
    }

    private void sendShippingNotificationEmail(Order order, String trackingNumber, String shippingCompany) {
        log.info("배송 알림 이메일 발송: 주문번호 {}, 운송장 번호 {}, 택배사 {}", order.getId(), trackingNumber, shippingCompany);
    }

    private void sendTrackingSms(Order order, String trackingNumber) {
        log.info("배송 추적 SMS 발송: 주문번호{}, 운송장 번호{}", order.getId(), trackingNumber);
    }

    private void sendCancellationEmail(Order order, String cancelReason) {
        log.info("주문 취소 이메일 발송: 주문번호{}, 취소사유{}", order.getId(), cancelReason);
    }

    private void sendRefundProcessEmail(Order order) {
        log.info("환불 처리 안내 이메일 발송: 주문 번호 {}", order.getId());
    }
}

