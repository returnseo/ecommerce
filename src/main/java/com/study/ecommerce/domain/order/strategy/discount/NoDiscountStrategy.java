package com.study.ecommerce.domain.order.strategy.discount;

import com.study.ecommerce.domain.order.entity.Order;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 *  할인 없음 정책
 */
@Component
public class NoDiscountStrategy implements DiscountStrategy{
    @Override
    public BigDecimal calculateDiscount(Order order) {
        return BigDecimal.ZERO;
    }

    @Override
    public String getDiscountPolicyName() {
        return "할인 없음";
    }

    @Override
    public boolean isApplicable(Order order) {
        return true;
    }
}
