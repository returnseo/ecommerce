package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

public class ExpressShippingStrategy implements ShippingStrategy{
    // 배송비
    private static final BigDecimal SHIPPING_COST = new BigDecimal("6000");
    // 당일인지 익일인지  = 1
    private static final int DELIVERY_DAYS = 1;

    @Override
    public BigDecimal calculateShippingCost(Order order) {
        return SHIPPING_COST;
    }

    @Override
    public String getShippingPolicyName() {
        return "익스프레스 배송 (당일/익일)";
    }

    @Override
    public int getEstimatedDeliveryDays(Order order) {
        return DELIVERY_DAYS;
    }
}
