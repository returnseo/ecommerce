package com.study.ecommerce.domain.payment.entity;

import com.study.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Payment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "order_id", nullable = false, unique = true)
    private Long orderId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentMethod paymentMethod;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PaymentStatus status;

    @Column(nullable = false)
    private Long amount;

    private LocalDateTime paidAt;

    @Builder
    public Payment(Long orderId, PaymentMethod paymentMethod, PaymentStatus status, Long amount) {
        this.orderId = orderId;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.amount = amount;
    }

    public void complete() {
        this.status = PaymentStatus.COMPLETED;
        this.paidAt = LocalDateTime.now();
    }

    public void updateStatus(PaymentStatus status) {
        this.status = status;
    }

    public enum PaymentMethod {
        CARD, BANK_TRANSFER, VIRTUAL_ACCOUNT
    }

    public enum PaymentStatus {
        PENDING, COMPLETED, CANCELED, REFUNDED
    }
}
