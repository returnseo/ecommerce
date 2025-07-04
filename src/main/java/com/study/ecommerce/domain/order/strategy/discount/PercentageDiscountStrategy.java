package com.study.ecommerce.domain.order.strategy.discount;

import com.study.ecommerce.domain.order.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 퍼센트 할인 정책
 */
@Component
public class PercentageDiscountStrategy implements DiscountStrategy{
    private static final BigDecimal DISCOUNT_PERCENTAGE = new BigDecimal("0.10");
    private static final BigDecimal MINIMUM_AMOUNT = new BigDecimal("50000");

    @Override
    public BigDecimal calculateDiscount(Order order) {
        if (!isApplicable(order)) {
            return BigDecimal.ZERO;
        }

        return order.getTotalAmount()
                .multiply(DISCOUNT_PERCENTAGE)
                .setScale(0, RoundingMode.DOWN);
    }

    @Override
    public String getDiscountPolicyName() {
        return "퍼센트 할인 (10%)";
    }

    @Override
    public boolean isApplicable(Order order) {
        return order.getTotalAmount().compareTo(MINIMUM_AMOUNT) >= 0;
    }
}
