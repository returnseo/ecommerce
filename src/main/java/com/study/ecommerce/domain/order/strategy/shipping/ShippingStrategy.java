package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public interface ShippingStrategy {

    BigDecimal calculateShippingCost(Order order);

    String getShippingPolicyName();

    int getEstimatedDeliveryDays(Order order);
}
