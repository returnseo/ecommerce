package com.study.ecommerce.domain.order.event;

import com.study.ecommerce.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderPaidEvent extends OrderEvent{
    private final String paymentMethod;

    public OrderPaidEvent(Object source, Order order, String paymentMethod) {
        super(source, order, "ORDER_PAID");
        this.paymentMethod = paymentMethod;
    }
}
