package com.study.ecommerce.infra.shipping.dto;

public record ShippingRequest(
        String orderId,
        String senderName,
        String senderPhone,
        String senderAddress,
        String receiverName,
        String receiverPhone,
        String receiverAddress,
        String receiverZipCode,
        int weight, // 그램 단위
        String packageType, // BOX, ENVELOPE, BAG
        boolean fragile,
        String deliveryMessage
) {
}
