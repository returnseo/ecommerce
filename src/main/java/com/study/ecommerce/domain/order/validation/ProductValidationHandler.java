package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.dto.OrderCreateRequest.OrderItemRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ProductValidationHandler extends OrderValidationHandler{
    // static 변수

    @Override
    protected void doValidate(OrderCreateRequest request) {
        for (OrderItemRequest orderItem : request.items()) {
            validateOrderItem(orderItem);
        }
    }

    private void validateOrderItem(OrderItemRequest orderItem) {
        // 상품 id 검증(db) ->

        // 수량 검증(db) ->

        // 최대 주문 수량 검증

        // 상품 가격 검증

        // 상품 존재 여부 검증 (실제로는 DB 참조)

        // 상품 판매 가능 여부 검증 (db)
    }

    @Override
    protected String getHandlerName() {
        return "상품검증핸들러";
    }


}
