package com.study.ecommerce.domain.order.event;

import com.study.ecommerce.domain.order.entity.Order;
import lombok.Getter;
import lombok.ToString;
import org.springframework.context.ApplicationEvent;

/**
 * 주문관련 이벤트의 기본클래스
 */
@Getter
@ToString
public abstract class OrderEvent extends ApplicationEvent {
    private final Order order;
    private final String eventType;

    public OrderEvent(Object source, Order order, String eventType) {
        super(source);
        this.order = order;
        this.eventType = eventType;
    }
}
