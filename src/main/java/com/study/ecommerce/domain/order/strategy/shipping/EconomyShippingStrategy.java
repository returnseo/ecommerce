package com.study.ecommerce.domain.order.strategy.shipping;

import com.study.ecommerce.domain.order.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 일반 배송
 */
@Component
public class EconomyShippingStrategy implements ShippingStrategy{
    private static final BigDecimal SHIPPING_COST = new BigDecimal("1500");
    private static final BigDecimal FREE_SHIPPING_THRESHOLD = new BigDecimal("30000");
    private static final int DELIVERY_DAYS = 5;

    @Override
    public BigDecimal calculateShippingCost(Order order) {
        if (order.getTotalAmount().compareTo(FREE_SHIPPING_THRESHOLD) >= 0) {
            return BigDecimal.ZERO;
        }
        return SHIPPING_COST;
    }

    @Override
    public String getShippingPolicyName() {
        return "이코노미 배송 (5-7일)";
    }

    @Override
    public int getEstimatedDeliveryDays(Order order) {
        return DELIVERY_DAYS;
    }
    // 배송비
    // 무료배송기준
    // 배송날짜
}
