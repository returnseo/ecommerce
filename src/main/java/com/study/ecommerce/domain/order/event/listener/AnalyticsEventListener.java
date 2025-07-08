package com.study.ecommerce.domain.order.event.listener;

import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AnalyticsEventListener {

    @EventListener
    @Async
    public void handleOrderCreated(OrderCreatedEvent event) {

        // 주문 생성 통계 수집
        collectOrderCreationStats(event.getOrder());
        // 상품별 주문 통계 업데이트
        updateProductOrderStats(event.getOrder());
    }

    private void collectOrderCreationStats(Order order) {
        // 실제 통계 서비스 구현을 해야함 AnalyticsService <-
        log.info("주문 생성 통계 수집: 주문번호: {}, 총액: {}", order.getId(), order.getTotalAmount());
    }

    private void updateProductOrderStats(Order order) {
        order.getOrderItems().forEach(item -> {
            log.debug("상품 {}, 주문량 증가: {}", item.getProduct().getId(), item.getQuantity());
        });
    }
}
