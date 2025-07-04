package com.study.ecommerce.infra.shipping.external;

import com.study.ecommerce.infra.payment.external.toss.TossPaymentResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

/**
 * CJ대한통운 외부 API (실제 API 호출 시뮬레이션)
 */
@Slf4j
@Component
public class CjShippingApi {


    private final Map<String, CjShippingResponse> shipping = new HashMap<>();

    /**
     * CJ대한통운 배송 등록
     */
    public CjShippingResponse registerDelivery(CjShippingRequest request) {
        log.info("CJ대한통운 API 호출: {}", request );

        //invoiceNo
        String invoiceNo = UUID.randomUUID().toString();

        //resultCode
        String resultCodeValid = Objects.equals(request.receiverName(), "01") ? "0000" :
                                 Objects.equals(request.receiverName(), "02") ? "0000" : "기타" ;

        //resultMessage
        String resultMessage = Objects.equals(request.receiverName(), "0000") ? "배송중입니다." : "배송에 실패하였습니다.";

        CjShippingResponse  cjShippingResponse = CjShippingResponse.builder()
                .resultCode(resultCodeValid)
                .resultMessage(resultMessage)
                .invoiceNo(invoiceNo)
                .orderNo(request.orderNo())
                .deliveryCharge(calculateDeliveryCharge(request))
                .build();

        shipping.put(invoiceNo, cjShippingResponse);

        return cjShippingResponse;
    }

    /**
     * 배송 상태 조회
     */
    public CjTrackingResponse getTrackingInfo(String invoiceNo) {

        CjShippingResponse response = shipping.get(invoiceNo);

        return CjTrackingResponse.builder()
                .resultCode(response.resultCode())
                .resultMessage(response.resultMessage())
                .invoiceNo(response.invoiceNo())
                .deliveryStatus(DeliveryStatus.getFirstStatusCode())
                .deliveryStatusName(DeliveryStatus.getDescriptionByCode("10"))//기본값 접수
                .currentLocation("현재 위치")
                .deliveryDateTime(LocalDateTime.now().toString())
                .build();
    }

    enum DeliveryStatus {
        RECEIPT("10", "접수"),
        DROP_OFF("20", "집하"),
        DELIVERY("30", "배송중"),
        COMPLETED("40", "배송완료");

        private final String code;
        private final String description;

        DeliveryStatus(String code, String description) {
            this.code = code;
            this.description = description;
        }

        public String getCode() {
            return code;
        }

        public String getDescription() {
            return description;
        }

        // deliveryStatus
        public static DeliveryStatus fromCode(String code) {
            for (DeliveryStatus status : values()) {
                if (status.code.equals(code)) {
                    return status;
                }
            }
            return null;
        }

        // deliveryStatusName
        public static String getDescriptionByCode(String code) {
            DeliveryStatus status = fromCode(code);
            return (status != null) ? status.getDescription() : null;
        }

        public static String getFirstStatusCode() {
            return values().length > 0 ? values()[0].getCode() : null;
        }
    }

    /**
     * 배송 취소
     */
    public CjShippingResponse cancelDelivery(String invoiceNo, String cancelReason) {

        CjShippingResponse response = shipping.get(invoiceNo);


        if (response == null) {
            return CjShippingResponse.builder()
                    .resultMessage("존재하지 않는 운송장 번호입니다.")
                    .build();
        }

        shipping.remove(invoiceNo);

        return CjShippingResponse.builder()
                .resultCode("CANCELLED")
                .resultMessage("배송이 취소되었습니다. 사유: " + cancelReason)
                .invoiceNo(invoiceNo)
                .orderNo(response.orderNo())
                .deliveryCharge(0)
                .build();
    }

    /**
     * 배송비 계산 (CJ 고유 로직)
     */
     public int calculateDeliveryCharge(CjShippingRequest request) {
        int baseCharge = 3000; // 기본 배송비
        int weight = request.weight();

        // 무게에 따른 추가 요금

        // 5kg 초과
         int PlusCharge = (weight <= 5000) ? 0 : 3000;

         int TotalCharge = (weight > 5000) ? baseCharge + PlusCharge : baseCharge;

        // 제주도/도서산간 추가 요금
         String ozzy = request.receiverAddr();

         int extraCharge = (ozzy.contains("제주도") || ozzy.contains("산간")) ? 3000 : 0;

         TotalCharge = (TotalCharge == 6000) ? TotalCharge : TotalCharge + extraCharge;

         return TotalCharge;
    }
}
