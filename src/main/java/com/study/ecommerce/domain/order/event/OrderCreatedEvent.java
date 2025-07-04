package com.study.ecommerce.domain.order.event;

import com.study.ecommerce.domain.order.entity.Order;

public class OrderCreatedEvent extends OrderEvent{

    public OrderCreatedEvent(Object source, Order order) {
        super(source, order, "ORDER_CREATED");
    }
}
