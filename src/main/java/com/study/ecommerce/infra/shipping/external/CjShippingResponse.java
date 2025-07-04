package com.study.ecommerce.infra.shipping.external;

import lombok.Builder;

/**
 * CJ대한통운 API 응답 DTO (실제 API 명세와 유사)
 */
@Builder
public record CjShippingResponse(
        String resultCode, // 0000: 성공, 기타: 실패
        String resultMessage,
        String invoiceNo, // 운송장번호
        String orderNo,
        int deliveryCharge
) {
}
