package com.study.ecommerce.infra.payment.external.toss;

import lombok.Builder;

@Builder
public record TossCancelRequest(
        int cancelAmount,
        String cancelReason
) {
}
