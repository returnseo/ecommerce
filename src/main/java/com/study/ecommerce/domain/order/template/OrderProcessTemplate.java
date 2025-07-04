package com.study.ecommerce.domain.order.template;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.entity.Order;
import lombok.extern.slf4j.Slf4j;

/**
 * 주문 처리 프로세스를 위한 Template Method Pattern 사용
 */
@Slf4j
public abstract class OrderProcessTemplate {

    public final Order processOrder(OrderCreateRequest request) {
        log.info("주문처리 시작: {}", getOrderType());

        try {
            // 1.  주문 전 검증
            validateOrderRequest(request);

            // 2. 재고 확인 및 ㅇ예약
            reserveInventory(request);

            // 3. 주문 생성
            Order order = createOrder(request);

            // 4.할인 적용
            applyDiscounts(order);

            // 5.배송비 계산
            calculateShipping(order);

            // 6. 결제처리
            processPayment(order);

            // 7. 주문 완료처리
            finalizeOrder(order);

            // 8. 후처리
            postProcess(order);

            log.info("주문 처리완료: {} - 주문번호: {}", getOrderType(), order.getId());
            return order;
        }catch (Exception e) {
            log.error("주문 처리 실패: {} - {}", "타입", e.getMessage());
            handleError(request, e);
            throw e;
        }
    }

    /**
     * 주문 유형을 반환
     * @return 주문유형
     */
    protected abstract String getOrderType();

    /**
     * 주문 요청을 검증
     * @param request 주문 생성 정보
     */
    protected abstract void validateOrderRequest(OrderCreateRequest request);

    /**
     * 재고 확인및 예약
     * @param request 주문생성 정보
     */
    protected abstract void reserveInventory(OrderCreateRequest request);

    /**
     * 주문 생성
     * @param request 주문 생성 정보
     * @return 주문 정보
     */
    protected abstract Order createOrder(OrderCreateRequest request);

    /**
     * 할인을 적용
     * @param order 주문정보
     */
    protected abstract void applyDiscounts(Order order);

    /**
     * 배송비 계산
     * @param order 주문정보
     */
    protected abstract void calculateShipping(Order order);

    /**
     * 결제를 처리
     * @param order 주문정보
     */
    protected abstract void processPayment(Order order);

    /**
     * 주문을 완료 처리
     * @param order 주문정보
     */
    protected abstract void finalizeOrder(Order order);

    protected void postProcess(Order order) {
        log.info("기본 후처리 완료: {}", order.getId());
    }

    protected void handleError(OrderCreateRequest request, Exception e) {
        log.error("기본 에러처리: {}", e.getMessage());
    }
}
