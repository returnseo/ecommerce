package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class MemberValidationHandler extends OrderValidationHandler{

    @Override
    protected void doValidate(OrderCreateRequest request) {
        Long memberId = request.memberId();
    }

    @Override
    protected String getHandlerName() {
        return "";
    }

    private boolean isMemberExists(Long memberId) {
        // memberService -> existsById(memberId)
        return true;
    }

    private boolean isMemberActive(Long memberId) {
        // isActive(memberId);
        return true;
    }

    private boolean hasOrderPermission(Long memberId) {
        // hasOrderPermission(memberId);
        return true;
    }

    private boolean isCreditWorthy(Long memberId) {
        // getCreditScore(memberId) >= MINIMUM_CREDIT_SCORE;
        return true;
    }

    private boolean exceedsDailyOrderLimit(Long memberId, OrderCreateRequest request) {
        // OrderService 에서 일일 주문내역확인
        // BigDecimal todayOrderAmount = orderService.getTodayOrderAmount(memberId);
        // BigDecimal requestAmount = calculateTotalAmount(request);
        // return todayOrderAmount.add(requestAmount).compareTo(DAILY_ORDER_LIMIT) > 0;
        return true;
    }
}
