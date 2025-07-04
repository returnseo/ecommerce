package com.study.ecommerce.infra.shipping.gateway;

import com.study.ecommerce.infra.shipping.dto.ShippingRequest;
import com.study.ecommerce.infra.shipping.dto.ShippingResponse;

public interface ShippingGateway {
    /**
     * 배송 등록
     */
    ShippingResponse registerShipping(ShippingRequest request);

    /**
     * 배송 상태 조회
     */
    ShippingResponse getShippingStatus(String trackingNumber);

    /**
     * 배송 취소
     */
    ShippingResponse cancelShipping(String trackingNumber, String reason);

    /**
     * 배송비 계산
     */
    int calculateShippingCost(ShippingRequest request);

    /**
     * 배송사 이름 반환
     */
    String getCarrierName();
}
