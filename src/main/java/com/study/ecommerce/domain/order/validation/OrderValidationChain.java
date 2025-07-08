package com.study.ecommerce.domain.order.validation;

import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.validation.OrderValidationHandler.ValidationException;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderValidationChain {

    private final BasicInfoValidationHandler basicInfoValidationHandler;
    private final MemberValidationHandler memberValidationHandler;
    private final ProductValidationHandler productValidationHandler;
    private final InventoryValidationHandler inventoryValidationHandler;
    private final PaymentValidationHandler paymentValidationHandler;
    private OrderValidationHandler firstHandler;

    /**
     *  검증 체인 구현
     */
    @PostConstruct
    public void buildChain() {
        firstHandler = basicInfoValidationHandler;
        basicInfoValidationHandler
                .setNext(memberValidationHandler)
                .setNext(productValidationHandler)
                .setNext(inventoryValidationHandler)
                .setNext(paymentValidationHandler);
    }

    public void validateOrder(OrderCreateRequest request) {
        long startTime = System.currentTimeMillis();

        try {
            if (firstHandler != null) {
                firstHandler.validate(request);
            }

            long endTime = System.currentTimeMillis();

            log.info("주문 유효성 검증 완료: 회원 ID {} (소요시간: {} ms)", request.memberId(), endTime - startTime);
        } catch (ValidationException e) {
            long endTime = System.currentTimeMillis();
            log.error("주문 유효성 검증 실패: 회원 ID {} (소요시간: {} ms)", request.memberId(), endTime- startTime);
            throw e;
        }

    }

    public void validateOrderPartial(OrderCreateRequest request, ValidationTypes... validationTypes) {
        for (ValidationTypes type : validationTypes) {
            switch (type) {
                case BASIC_INFO -> basicInfoValidationHandler.validate(request);
                case MEMBER -> memberValidationHandler.validate(request);
                case PRODUCT -> productValidationHandler.validate(request);
                case INVENTORY -> inventoryValidationHandler.validate(request);
                case PAYMENT -> paymentValidationHandler.validate(request);
            }
        }
    }

    public void rebuildChain(OrderValidationHandler... handlers) {
        if (handlers.length == 0) {
            firstHandler = null;
            return;
        }

        firstHandler = handlers[0];
        for (int i = 0; i < handlers.length - 1; i++) {
            handlers[i].setNext(handlers[i + 1]);
        }
    }

    public enum ValidationTypes {
        BASIC_INFO, MEMBER, PRODUCT, INVENTORY, PAYMENT
    }

}
