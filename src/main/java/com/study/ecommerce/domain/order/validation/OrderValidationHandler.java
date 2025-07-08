package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;

/**
 * 주문 유효성 검증을 위한 Chain of Responsibility 패턴의 인터페이스
 */
public abstract class OrderValidationHandler {
    private OrderValidationHandler nextHandler;

    /**
     * 다음 핸들러 설정
     * @param nextHandler 다음 핸들러
     * @return 현재 핸들러 (체이닝을 위함)
     */
    public OrderValidationHandler setNext(OrderValidationHandler nextHandler) {
        this.nextHandler = nextHandler;
        return nextHandler;
    }

    public final void validate(OrderCreateRequest request) {
        doValidate(request);

        if (nextHandler != null) {
            nextHandler.validate(request);
        }
    }

    /**
     * 실제 유효성 검증 로직 구현
     * @param request 주문 생성 요청
     * @throws jakarta.validation.ValidationException 검증 실패시
     */
    protected abstract void doValidate(OrderCreateRequest request);

    protected abstract String getHandlerName();

    protected void fail(String message) {
        throw new ValidationException(String.format("[%s] %s", getHandlerName(), message));
    }

    public static class ValidationException extends RuntimeException {
        public ValidationException(String message) {super(message);}
    }
}
