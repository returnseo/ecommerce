package com.study.ecommerce.domain.order.event.listener;

import com.study.ecommerce.domain.order.event.OrderCreatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.core.annotation.Order;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryEventListener {

    @EventListener
    @Order(1) // 높은 숫자일수록 우선순위가 높습니다.
    public void handleOrderCreated(OrderCreatedEvent event) {
        // 로그찍기
    }

}
