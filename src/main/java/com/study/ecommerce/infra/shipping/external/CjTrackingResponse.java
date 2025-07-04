package com.study.ecommerce.infra.shipping.external;

import lombok.Builder;

/**
 * CJ대한통운 배송 조회 응답 DTO
 */
@Builder
public record CjTrackingResponse(
        String resultCode,
        String resultMessage,
        String invoiceNo,
        String deliveryStatus, // 10:접수, 20:집하, 30:배송중, 40:배송완료
        String deliveryStatusName,
        String currentLocation,
        String deliveryDateTime
) {
}
