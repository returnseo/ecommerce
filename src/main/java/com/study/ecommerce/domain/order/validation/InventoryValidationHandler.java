package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.dto.OrderCreateRequest.OrderItemRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class InventoryValidationHandler extends OrderValidationHandler{

    @Override
    protected void doValidate(OrderCreateRequest request) {
        // for문 ->
        for (OrderItemRequest item : request.items()) {
            validateInventory(item);
        }
    }

    private void validateInventory(OrderItemRequest item) {
        // 현재 재고 조회 ->  변수할당
        // 재고 부족 검증

        // 예약된 재고 고려

    }

    @Override
    protected String getHandlerName() {
        return "";
    }

    private int getAvailableStock(Long productId) {
        return 90;
    }
}
