package com.study.ecommerce.domain.order.service;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.order.event.OrderCreatedEvent;
import com.study.ecommerce.domain.order.event.OrderPaidEvent;
import com.study.ecommerce.domain.order.template.OrderProcessTemplate;
import com.study.ecommerce.domain.order.template.PremiumOrderProcessor;
import com.study.ecommerce.domain.order.template.RegularOrderProcessor;
import com.study.ecommerce.domain.order.validation.OrderValidationChain;
import com.study.ecommerce.domain.order.validation.OrderValidationHandler;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class OrderPatternService {
    private final RegularOrderProcessor regularOrderProcessor;
    private final PremiumOrderProcessor premiumOrderProcessor;
    private final ApplicationEventPublisher eventPublisher;
    private final OrderValidationChain validationChain;

    public Order createOrder(OrderCreateRequest request) {
        try {
            validationChain.validateOrder(request);

            OrderProcessTemplate orderProcessTemplate = selectOrderProcessor(request);
            Order order = orderProcessTemplate.processOrder(request);

            publishOrderCreatedEvent(order);
            publishOrderPaidEvent(order, request.paymentMethod());

            return order;
        } catch (Exception e) {
            log.error("에러메시지: {}", e.getMessage());
            throw e;
        }
    }

    private OrderProcessTemplate selectOrderProcessor(OrderCreateRequest request) {
        boolean isVipMember = isVipMember(request.memberId());

        if (isVipMember) {
            return premiumOrderProcessor;
        } else {
            return regularOrderProcessor;
        }
    }

    private boolean isVipMember(Long memberId) {
        return memberId % 10 == 0;
    }

    private void publishOrderCreatedEvent(Order order) {
        OrderCreatedEvent event = new OrderCreatedEvent(this, order);
        eventPublisher.publishEvent(event);
    }

    private void publishOrderPaidEvent(Order order, String paymentMethod) {
        OrderPaidEvent orderPaidEvent = new OrderPaidEvent(this, order, paymentMethod);
        eventPublisher.publishEvent(orderPaidEvent);
    }

}
