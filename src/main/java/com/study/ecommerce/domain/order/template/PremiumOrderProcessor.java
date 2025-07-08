package com.study.ecommerce.domain.order.template;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.order.strategy.discount.DiscountStrategy;
import com.study.ecommerce.domain.order.strategy.shipping.ShippingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PremiumOrderProcessor extends OrderProcessTemplate{

    private final List<DiscountStrategy> discountStrategies;
    private final List<ShippingStrategy> shippingStrategies;

    @Override
    protected String getOrderType() {
        return "프리미엄 주문";
    }

    @Override
    protected void validateOrderRequest(OrderCreateRequest request) {
        log.info("프리미엄 주문 요청 검증 시작");
        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 없습니다.");
        }

        log.info("프리미엄 주문 요청 검증 완료");
    }

    @Override
    protected void reserveInventory(OrderCreateRequest request) {
        request.items().forEach(item -> {
            log.debug("VIP 우선 재고 예약 - 상품 {} : {} 개", item.getProductId(), item.getQuantity());
        });
    }

    @Override
    protected Order createOrder(OrderCreateRequest request) {
        log.info("VIP 주문 생성 시작");
        Order order = Order.builder()
                .memberId(request.memberId())
                .status(Order.OrderStatus.CREATED)
                .orderDate(LocalDateTime.now())
                .totalAmount(calculateTotalAmount(request))
                .build();

        order.updateShippingAddress(request.shippingAddress());
        order.updatePhoneNumber(request.phoneNumber());
        log.info("VIP 주문 생성 완료");
        return order;
    }

    @Override
    protected void applyDiscounts(Order order) {
        log.info("할인 적용 시작");
        BigDecimal maxDiscount = BigDecimal.ZERO;
        String appliedPolicy = "없음";

        for (DiscountStrategy strategy : discountStrategies) {
            if (strategy.isApplicable(order)) {
                BigDecimal discount = strategy.calculateDiscount(order);

                if (discount.compareTo(maxDiscount) > 0) {
                    maxDiscount = discount;
                    appliedPolicy = strategy.getDiscountPolicyName();
                }
            }
        }

        order.updateDiscountAmount(maxDiscount);
        log.info("할인 적용 완료: {} - {} 원", appliedPolicy, maxDiscount);
    }

    @Override
    protected void calculateShipping(Order order) {

    }

    @Override
    protected void processPayment(Order order) {

    }

    @Override
    protected void finalizeOrder(Order order) {

    }

    private BigDecimal calculateTotalAmount(OrderCreateRequest request) {
        if (request.items() == null) {
            return BigDecimal.ZERO;
        }

        return request.items().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
