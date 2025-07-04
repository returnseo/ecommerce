package com.study.ecommerce.domain.order.strategy.discount;

import com.study.ecommerce.domain.order.entity.Order;

import java.math.BigDecimal;

/**
 * 할인 정책을 위한 정책 인터페이스
 *
 */
public interface DiscountStrategy {

    /**
     *  할인 금액을 계산
     */
    BigDecimal calculateDiscount(Order order);

    /**
     * 할인 정책의 이름을 반환
     * @return 할인 정책명
     */
    String getDiscountPolicyName();

    /**
     * 할인 정책이 적용 가능한지 확인
     * @param order 주문정보
     * @return 적용 가능 여부
     */
    boolean isApplicable(Order order);
}
