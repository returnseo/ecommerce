package com.study.ecommerce.domain.order.entity;

import com.study.ecommerce.global.common.BaseTimeEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Order extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime orderDate;

    private BigDecimal totalAmount = BigDecimal.ZERO;

    // db에 저장하지는 않는다.
    @Transient
    private BigDecimal discountAmount = BigDecimal.ZERO;

    @Transient
    private BigDecimal shippingCost = BigDecimal.ZERO;

    @Transient
    private String shippingAddress;

    @Transient
    private String phoneNumber;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> orderItems = new ArrayList<>();

    @Builder
    public Order(Long memberId, OrderStatus status, LocalDateTime orderDate, BigDecimal totalAmount) {
        this.memberId = memberId;
        this.status = status;
        this.orderDate = orderDate;
        this.totalAmount = totalAmount != null ? totalAmount : BigDecimal.ZERO;
    }

    // 비지니스 메소드
    public void updateStatus(OrderStatus status) {
        this.status = status;
    }

    public void updateTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void updateShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void updateDiscountAmount(BigDecimal maxDiscount) {
        this.discountAmount = maxDiscount;
    }

    public enum OrderStatus {
        CREATED, PAID, CANCELED, SHIPPING, CONFIRMED, DELIVERED
    }
}
