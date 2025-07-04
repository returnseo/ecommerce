package com.study.ecommerce.domain.order.template;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.order.repository.OrderRepository;
import com.study.ecommerce.domain.order.strategy.discount.DiscountStrategy;
import com.study.ecommerce.domain.order.strategy.shipping.ShippingStrategy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 일반 주문 처리기
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class RegularOrderProcessor extends OrderProcessTemplate{

    private final List<DiscountStrategy> discountStrategies;
    private final List<ShippingStrategy> shippingStrategies;
    private final OrderRepository orderRepository;

    @Override
    protected String getOrderType() {
        return "일반 주문";
    }

    @Override
    protected void validateOrderRequest(OrderCreateRequest request) {
        log.info("일반 주문 요청 검증 시작");

        if (request.items() == null || request.items().isEmpty()) {
            throw new IllegalArgumentException("주문 상품이 없습니다.");
        }

        request.items().forEach(item -> {
            if (item.getQuantity() <= 0) {
                throw new IllegalArgumentException("상품 수량은 1개 이상이어야 합니다.");
            }
        });

        log.info("일반 주문 요청 검증 완료");
    }

    @Override
    protected void reserveInventory(OrderCreateRequest request) {
        log.info("재고 예약 시작");

        request.items().forEach(item -> {
            log.debug("상품 {} 재고 예약: {}개", item.getProductId(), item.getQuantity());
        });

        log.info("재고 예약 완료");
    }

    @Override
    protected Order createOrder(OrderCreateRequest request) {
        log.info("주문 생성 시작");
        Order order = Order.builder()
                .memberId(request.memberId())
                .status(Order.OrderStatus.CREATED)
                .orderDate(LocalDateTime.now())
                .totalAmount(calculateTotalAmount(request))
                .build();

        order.updateShippingAddress(request.shippingAddress());
        order.updatePhoneNumber(request.phoneNumber());
        log.info("주문 생성 완료");
        return order;
    }

    private BigDecimal calculateTotalAmount(OrderCreateRequest request) {
        if (request.items() == null) {
            return BigDecimal.ZERO;
        }

        return request.items().stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
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
        log.info("배송비 계산 시작");
        log.info("배송비 계산 완료");
    }

    @Override
    protected void processPayment(Order order) {
        log.info("결제처리 시작");
        BigDecimal totalAmount = order.getTotalAmount()
                .subtract(order.getDiscountAmount())
                .add(order.getShippingCost());

        log.info("결제처리 완료: {}원", totalAmount);

    }

    @Override
    protected void finalizeOrder(Order order) {
        log.info("주문 완료 처리 시작");
        order.updateStatus(Order.OrderStatus.CONFIRMED);
        // repository에 저장
        orderRepository.save(order);

        log.info("주문 완료 처리 완료");
    }

    @Override
    protected void postProcess(Order order) {
        super.postProcess(order);

        log.info("일반 주문 후처리: 주문 확인 이메일 발송");

        // 이메일 발송 로직
    }
}
