package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Slf4j
@Component
public class BasicInfoValidationHandler extends OrderValidationHandler{

    @Override
    protected void doValidate(OrderCreateRequest request) {
        // 회원 ID 검증
        if (request.memberId() == null || request.memberId() <= 0) {
            fail("회원 ID가 유효하지 않습니다.");
        }

        // 주문 상품 목록 검증
        if (request.items() == null || request.items().isEmpty()) {
            fail("주문할 상품이 없습니다.");
        }

        // 배송지 정보 검증
        if (request.shippingAddress() == null || request.shippingAddress().trim().isEmpty()) {
            fail("배송지 정보가 없습니다.");
        }

        // 연락처 검증
        if (request.phoneNumber() == null || request.phoneNumber().trim().isEmpty()) {
            fail("연락처 정보가 없습니다.");
        }

        // 연락처 형식 검증
        if (!isValidPhoneNumber(Objects.requireNonNull(request.phoneNumber()))) {
            fail("연락처 형식이 올바르지 않습니다.");
        }
    }

    @Override
    protected String getHandlerName() {
        return "기본정보검증";
    }

    private boolean isValidPhoneNumber(String phoneNumber) {
        return phoneNumber.matches("^01[016789]-?\\d{3,4}-?\\d{4}$");
    }
}
