package com.study.ecommerce.infra.shipping.external;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * CJ대한통운 외부 API (실제 API 호출 시뮬레이션)
 */
@Slf4j
@Component
public class CjShippingApi {
    /**
     * CJ대한통운 배송 등록
     */
    public CjShippingResponse registerDelivery(CjShippingRequest request) {
        // TODO
        return null;
    }

    /**
     * 배송 상태 조회
     */
    public CjTrackingResponse getTrackingInfo(String invoiceNo) {
        // TODO
        return null;
    }

    /**
     * 배송 취소
     */
    public CjShippingResponse cancelDelivery(String invoiceNo, String cancelReason) {
        // TODO
        return null;
    }

    /**
     * 배송비 계산 (CJ 고유 로직)
     */
    private int calculateDeliveryCharge(CjShippingRequest request) {
        int baseCharge = 3000; // 기본 배송비

        // 무게에 따른 추가 요금
        // 5kg 초과


        // 제주도/도서산간 추가 요금

        return baseCharge;
    }
}
