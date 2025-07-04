package com.study.ecommerce.domain.order.strategy.discount;

import com.study.ecommerce.domain.order.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * 고정 금액 할인 정책
 */
@Component
public class FixedAmountDiscountStrategy implements DiscountStrategy{
    private static final BigDecimal DISCOUNT_AMOUNT = new BigDecimal("5000");
    private static final BigDecimal MINIMUM_AMOUNT = new BigDecimal("30000");


    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (!isApplicable(order)) {
            return BigDecimal.ZERO;
        }
        return DISCOUNT_AMOUNT;
    }

    @Override
    public String getDiscountPolicyName() {
        return "고정 할인 (5,000원)";
    }

    @Override
    public boolean isApplicable(Order order) {
        return order.getTotalAmount().compareTo(MINIMUM_AMOUNT) >= 0;
    }
}
