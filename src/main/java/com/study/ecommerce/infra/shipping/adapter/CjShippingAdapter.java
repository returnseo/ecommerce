package com.study.ecommerce.infra.shipping.adapter;

import com.study.ecommerce.infra.shipping.dto.ShippingRequest;
import com.study.ecommerce.infra.shipping.dto.ShippingResponse;
import com.study.ecommerce.infra.shipping.external.CjShippingApi;
import com.study.ecommerce.infra.shipping.external.CjShippingRequest;
import com.study.ecommerce.infra.shipping.external.CjShippingResponse;
import com.study.ecommerce.infra.shipping.external.CjTrackingResponse;
import com.study.ecommerce.infra.shipping.gateway.ShippingGateway;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * Adapter Pattern: CJ대한통운 어댑터
 *
 * CJ대한통운의 고유한 API를 우리 시스템의 공통 인터페이스에 맞게 변환
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class CjShippingAdapter implements ShippingGateway {
    private final CjShippingApi cjShippingApi;

    @Override
    public ShippingResponse registerShipping(ShippingRequest request) {
        log.info("CJ대한통운 어댑터 - 배송 등록 요청 변환 및 처리");

        // 1. 우리 시스템의 요청을 CJ API 형태로 변환

        // 2. CJ API 호출

        // 3. CJ 응답을 우리 시스템 형태로 변환
        return null;
    }

    @Override
    public ShippingResponse getShippingStatus(String trackingNumber) {
        log.info("CJ대한통운 어댑터 - 배송 상태 조회: {}", trackingNumber);
        return null;
    }

    @Override
    public ShippingResponse cancelShipping(String trackingNumber, String reason) {
        log.info("CJ대한통운 어댑터 - 배송 취소: {}, 사유: {}", trackingNumber, reason);

        return null;
    }

    @Override
    public int calculateShippingCost(ShippingRequest request) {
        // CJ API를 통해 실제 배송비 계산 (여기서는 간단 계산)

        return 0;
    }

    @Override
    public String getCarrierName() {
        return "CJ대한통운";
    }

    /**
     * 공통 요청을 CJ API 요청으로 변환
     */
    private CjShippingRequest convertToCjRequest(ShippingRequest request) {
        return null;
    }

    /**
     * 패키지 타입 변환
     */
    private String convertPackageType(String packageType) {
        return switch (packageType) {
            case "BOX" -> "1";
            case "ENVELOPE" -> "2";
            case "BAG" -> "3";
            default -> "1";
        };
    }

    /**
     * CJ API 응답을 공통 응답으로 변환
     */
    private ShippingResponse convertToShippingResponse(CjShippingResponse cjResponse) {
        return null;
    }

    /**
     * CJ 배송 조회 응답을 공통 응답으로 변환
     */
    private ShippingResponse convertTrackingToShippingResponse(CjTrackingResponse cjResponse) {
        return null;
    }

    /**
     * CJ 배송 상태를 공통 상태로 변환
     */
    private String convertCjStatus(String cjStatus) {
        if (cjStatus == null) return "REGISTERED";

        return switch (cjStatus) {
            case "10" -> "REGISTERED"; // 접수
            case "20" -> "PICKED_UP";  // 집하
            case "30" -> "IN_TRANSIT"; // 배송중
            case "40" -> "DELIVERED";  // 배송완료
            default -> "REGISTERED";
        };
    }
}
