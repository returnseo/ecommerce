package com.study.ecommerce.domain.order.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

public record OrderCreateRequest(
        @NotNull
        Long memberId,

        List<Long> cartItemIds,

        @Valid
        @NotEmpty
        List<OrderItemRequest> items,

        @NotNull
        String shippingAddress,

        @NotNull
        String phoneNumber,

        @NotNull
        Boolean payNow,

        @NotNull
        String paymentMethod
) {

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemRequest {
        @NotNull
        private Long productId;

        @NotNull
        @Positive
        private Integer quantity;

        private String productName;
        private BigDecimal price;
    }
}
