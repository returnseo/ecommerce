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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

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
        CjShippingRequest cjShipRequest = convertToCjRequest(request);

        // 2. CJ API 호출
        CjShippingResponse cjShipResponse = cjShippingApi.registerDelivery(cjShipRequest);

        // 3. CJ 응답을 우리 시스템 형태로 변환
        return convertToShippingResponse(cjShipResponse);
    }

    @Override
    public ShippingResponse getShippingStatus(String trackingNumber) {
        log.info("CJ대한통운 어댑터 - 배송 상태 조회: {}", trackingNumber);

        CjTrackingResponse cjTrackResponse = cjShippingApi.getTrackingInfo(trackingNumber);

        return convertTrackingToShippingResponse(cjTrackResponse);
    }

    @Override
    public ShippingResponse cancelShipping(String trackingNumber, String reason) {
        log.info("CJ대한통운 어댑터 - 배송 취소: {}, 사유: {}", trackingNumber, reason);

        CjShippingResponse cjShipResponse = cjShippingApi.cancelDelivery(trackingNumber, reason);

        return convertToShippingResponse(cjShipResponse);
    }

    @Override
    public int calculateShippingCost(ShippingRequest request) {
        // CJ API를 통해 실제 배송비 계산 (여기서는 간단 계산)
        CjShippingRequest cjShipRequest = convertToCjRequest(request);

        return cjShippingApi.calculateDeliveryCharge(cjShipRequest);
    }

    @Override
    public String getCarrierName() {
        return "CJ대한통운";
    }

    /**
     * 공통 요청을 CJ API 요청으로 변환
     */
    private CjShippingRequest convertToCjRequest(ShippingRequest request) {
        return CjShippingRequest.builder()
                .orderNo(request.orderId())
                .senderName(request.senderName())
                .senderTel(request.senderPhone())
                .senderAddr(request.senderAddress())
                .receiverName(request.receiverName())
                .receiverTel(request.receiverPhone())
                .receiverAddr(request.receiverAddress())
                .receiverZipCode(request.receiverZipCode())
                .weight(request.weight())
                .boxType(request.packageType())
                .deliveryMessage(request.deliveryMessage())
                .build();
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
//    private ShippingResponse convertToShippingResponse(CjShippingResponse cjResponse) {
      private ShippingResponse convertToShippingResponse(CjShippingResponse cjResponse) {



        return ShippingResponse.builder()
                .success(Objects.equals(cjResponse.resultCode(), "0000"))
                .trackingNumber(cjResponse.invoiceNo())
                .status("")
                .message(cjResponse.resultMessage())
                .shippingCost(cjResponse.deliveryCharge())
                .estimatedDeliveryDate(LocalDateTime.now())
                .carrierName(getCarrierName())
                .errorCode(Objects.equals(cjResponse.resultCode(), "0000") ? null : Objects.equals(cjResponse.resultCode(), "기타") ? null : "실패")
                .build();
    }

    /**
     * CJ 배송 조회 응답을 공통 응답으로 변환
     */
    private ShippingResponse convertTrackingToShippingResponse(CjTrackingResponse cjResponse) {

        String deliveryDateTimeStr = cjResponse.deliveryDateTime();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        LocalDateTime estimatedDeliveryDate = LocalDateTime.parse(deliveryDateTimeStr, formatter);

        return ShippingResponse.builder()
                .success(Objects.equals(cjResponse.resultCode(), "0000"))
                .trackingNumber(cjResponse.invoiceNo())
                .status(convertCjStatus(cjResponse.deliveryStatus()))
                .message(cjResponse.resultMessage())
                .estimatedDeliveryDate(estimatedDeliveryDate)
                .carrierName(getCarrierName())
                .errorCode(Objects.equals(cjResponse.resultCode(), "0000") ? null : cjResponse.resultCode())
                .build();
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
