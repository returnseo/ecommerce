package com.study.ecommerce.domain.order.event;

import com.study.ecommerce.domain.order.entity.Order;
import lombok.Getter;

@Getter
public class OrderShippedEvent extends OrderEvent{
    private final String trackingNumber;
    private final String shippingCompany;

    public OrderShippedEvent(Object source, Order order, String trackingNumber, String shippingCompany) {
        super(source, order, "ORDER_SHIPPED");
        this.trackingNumber = trackingNumber;
        this.shippingCompany = shippingCompany;
    }
}
