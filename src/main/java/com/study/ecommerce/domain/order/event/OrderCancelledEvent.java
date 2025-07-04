package com.study.ecommerce.domain.order.event;

import com.study.ecommerce.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderCancelledEvent extends OrderEvent{
    private final String cancelReason;

    public OrderCancelledEvent(Object source, Order order, String cancelReason) {
        super(source, order, "ORDER_CANCELED");
        this.cancelReason = cancelReason;
    }
}
