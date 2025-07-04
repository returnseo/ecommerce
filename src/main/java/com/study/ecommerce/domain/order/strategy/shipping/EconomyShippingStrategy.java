package com.study.ecommerce.domain.order.strategy.shipping;

import java.math.BigDecimal;

import com.study.ecommerce.domain.order.entity.Order;

public class EconomyShippingStrategy implements ShippingStrategy {
    // 배송비
    private static final BigDecimal DELIVERY_COST = new BigDecimal("3000");
    private static final BigDecimal MINIMUM_AMOUNT= new BigDecimal("40000");

    // 무료배송기준
    @Override
    public BigDecimal calculateShippingCost(Order order) {
        if (order.getTotalAmount().compareTo(MINIMUM_AMOUNT) >= 0) {
            return BigDecimal.ZERO;
        }
        return DELIVERY_COST;
    }

    @Override
    public String getShippingPolicyName() {
        return "기본 배송";
    }

    // 배송날짜
    @Override
    public int getEstimatedDeliveryDays(Order order) {
        return order.getTotalAmount().compareTo(DELIVERY_COST) > 0 ? 3 : 5;
    }
}
