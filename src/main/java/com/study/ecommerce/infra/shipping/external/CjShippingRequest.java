package com.study.ecommerce.infra.shipping.external;

import lombok.Builder;

/**
 * CJ대한통운 API 요청 DTO (실제 API 명세와 유사)
 */
@Builder
public record CjShippingRequest(
        String orderNo,
        String senderName,
        String senderTel,
        String senderAddr,
        String receiverName,
        String receiverTel,
        String receiverAddr,
        String receiverZipCode,
        int weight, // 그램 단위
        String boxType, // 1:박스, 2:봉투, 3:기타
        String specialService, // 01:일반, 02:당일배송
        String deliveryMessage
) {
}
