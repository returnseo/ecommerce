package com.study.ecommerce.domain.order.strategy.shipping;

import java.math.BigDecimal;

public class ExpressShippingStrategy {
    // 배송비
    private static final BigDecimal DELIVERY_COST = new BigDecimal("10000");
    private static final BigDecimal MINIMUM_AMOUNT = new BigDecimal("100000");

    // 당일인지 익일인지  = 1
}
