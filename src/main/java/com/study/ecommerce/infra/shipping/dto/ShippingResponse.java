package com.study.ecommerce.infra.shipping.dto;

import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ShippingResponse(
        boolean success,
        String trackingNumber,
        String status, // REGISTERED, PICKED_UP, IN_TRANSIT, DELIVERED, CANCELLED
        String message,
        int shippingCost,
        LocalDateTime estimatedDeliveryDate,
        String carrierName,
        String errorCode
) {
}
