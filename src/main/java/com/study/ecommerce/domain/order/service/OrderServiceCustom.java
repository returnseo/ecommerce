package com.study.ecommerce.domain.order.service;

import com.study.ecommerce.domain.cart.entity.Cart;
import com.study.ecommerce.domain.cart.entity.CartItem;
import com.study.ecommerce.domain.cart.repository.CartItemRepository;
import com.study.ecommerce.domain.cart.repository.CartRepository;
import com.study.ecommerce.domain.member.entity.Member;
import com.study.ecommerce.domain.member.repository.MemberRepository;
import com.study.ecommerce.domain.order.dto.OrderCreateRequest;
import com.study.ecommerce.domain.order.dto.OrderCreateRequest.OrderItemRequest;
import com.study.ecommerce.domain.order.dto.OrderDetailResponse;
import com.study.ecommerce.domain.order.dto.OrderItemDto;
import com.study.ecommerce.domain.order.dto.OrderResponse;
import com.study.ecommerce.domain.order.entity.Order;
import com.study.ecommerce.domain.order.entity.Order.OrderStatus;
import com.study.ecommerce.domain.order.entity.OrderItem;
import com.study.ecommerce.domain.order.repository.OrderItemRepository;
import com.study.ecommerce.domain.order.repository.OrderRepository;
import com.study.ecommerce.domain.payment.entity.Payment;
import com.study.ecommerce.domain.payment.repository.PaymentRepository;
import com.study.ecommerce.domain.product.entity.Product;
import com.study.ecommerce.domain.product.repository.ProductRepository;
import com.study.ecommerce.global.error.exception.EntityNotFoundException;
import com.study.ecommerce.infra.payment.service.MockPaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static com.study.ecommerce.domain.order.entity.Order.OrderStatus.CREATED;

@Service
@RequiredArgsConstructor
public class OrderServiceCustom implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PaymentRepository paymentRepository;
    private final MockPaymentService mockPaymentService;

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public OrderResponse createOrder(OrderCreateRequest request, String email) {
        // 1. 회원 조회
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        // 2. 주문 생성(초기 총액자체를 0원으로 설정)
        Order order = Order.builder()
                .memberId(member.getId())
                .status(CREATED)
                .orderDate(LocalDateTime.now())
                .totalAmount(BigDecimal.valueOf(0L))
                .build();

        order = orderRepository.save(order);

        // 3. 주문 상품 처리 및 총액 계산
        long totalAmount = 0L;
        if (request.cartItemIds() != null && !request.cartItemIds().isEmpty()) {
            // 장바구니로 상품을 주문
            totalAmount = processCartItems(order, request.cartItemIds(), member);
        } else if (request.items() != null && !request.items().isEmpty()) {
            // 직접 지정한 상품
            totalAmount = processDirectItems(order, request.items());
        } else {
            throw new IllegalArgumentException("주문할 상품이 지정되지 않았습니다.");
        }

        // 4. 총액 업데이트
        order.updateTotalAmount(BigDecimal.valueOf(totalAmount));
        order = orderRepository.save(order);

        // 5. 결제 진행 (비동기로 처리해도됩니다)
        if (request.payNow()) {
            Payment payment = mockPaymentService.processPayment(order, Payment.PaymentMethod.valueOf(request.paymentMethod()));
            order.updateStatus(OrderStatus.PAID);
            orderRepository.save(order);
        }

        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount().longValue());
    }

    @Override
    @Transactional
    public OrderResponse cancelOrder(Long orderId, String email) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));

        // 주문자 확인
        Member member = memberRepository.findById(order.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        if (!member.getEmail().equals(email)) {
            throw new IllegalArgumentException("주문 취소 권한이 없습니다.");
        }

        // 주문 상태 확인
        if (order.getStatus() == OrderStatus.SHIPPING || order.getStatus() == OrderStatus.DELIVERED) {
            throw new IllegalArgumentException("배송 중이거나 배송이 완료된 주문은 취소할 수 없습니다.");
        }

        // 결제 취소 (결제가 완료된 경우)
        if (order.getStatus() == OrderStatus.PAID) {
            Payment payment = paymentRepository.findByOrderId(order.getId())
                    .orElseThrow(() -> new EntityNotFoundException("결제 정보를 찾을 수 없습니다."));
            mockPaymentService.cancelPayment(payment);

            List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
            for (OrderItem item : orderItems) {
                Product product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

                product.increasesStock(item.getQuantity());
                productRepository.save(product);
            }

            // 주문 상태를 변경
            order.updateStatus(OrderStatus.CANCELED);
        }

        orderRepository.save(order);

        return new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount().longValue());
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDetailResponse getOrderDetail(Long orderId, String email) {
        // 주문 조회
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다. id = " + orderId));

        // 주문자 확인
        Member member = memberRepository.findById(order.getMemberId())
                .orElseThrow(() -> new EntityNotFoundException("회원을 찾을 수 없습니다."));

        if (!member.getEmail().equals(email)) {
            throw new IllegalArgumentException("주문 조회 권한이 없습니다.");
        }

        // 주문 상품 조회
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(order.getId());
        List<OrderItemDto> orderItemDtos = orderItems.stream()
                .map(item -> {
                    Product product = productRepository.findById(item.getProduct().getId())
                            .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));
                    return new OrderItemDto(
                            product.getId(),
                            product.getName(),
                            item.getQuantity(),
                            item.getPrice().longValue()
                    );
                })
                .toList();

        return new OrderDetailResponse(
                order.getId(),
                member.getId(),
                member.getName(),
                order.getStatus(),
                order.getOrderDate(),
                order.getTotalAmount().longValue(),
                orderItemDtos
        );
    }

    @Override
    public Page<OrderResponse> getOrders(String email, Pageable pageable) {
        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new EntityNotFoundException("사용자를 찾을 수 없습니다."));

        Page<Order>orders = orderRepository.findByMemberId(member.getId(), pageable);

        return orders.map(order -> new OrderResponse(order.getId(), order.getStatus(), order.getTotalAmount().longValue()));
    }

    private long processCartItems(Order order, List<Long> cartItemIds, Member member) {
        Cart cart = cartRepository.findByMemberId(member.getId())
                .orElseThrow(() -> new EntityNotFoundException("장바구니를 찾을 수 없습니다."));

        long totalAmount = 0L;

        for (Long cartItemId : cartItemIds) {
            CartItem cartItem = cartItemRepository.findById(cartItemId)
                    .orElseThrow(() -> new EntityNotFoundException("장바구니 상품을 찾을 수 없습니다." + cartItemId));

            // 해당 장바구니 상품이 현재 사용자의 것인지
            if (!cartItem.getCartId().equals(cart.getId())) {
                throw new IllegalArgumentException("장바구니 상품 접근 권한이 없습니다.");
            }

            Product product = productRepository.findByIdWithPessimisticLock(cartItem.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

            product.decreasesStock(cartItem.getQuantity());
            productRepository.save(product);

            // 주문 상품 추가
            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(cartItem.getQuantity())
                    .price(BigDecimal.valueOf(product.getPrice()))
                    .build();

            orderItemRepository.save(orderItem);
            totalAmount += orderItem.getTotalPrice().longValue();

            // 주문한 상품은 장바구니에서 제거
            cartItemRepository.delete(cartItem);
        }
        return totalAmount;
    }

    private long processDirectItems(Order order, List<OrderItemRequest> items) {
        long totalAmount = 0L;

        for (OrderItemRequest request : items) {
            // 상품 재고 확인 및 감소
            Product product = productRepository.findByIdWithPessimisticLock(request.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("상품을 찾을 수 없습니다."));

            product.decreasesStock(request.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(BigDecimal.valueOf(product.getPrice()))
                    .build();

            orderItemRepository.save(orderItem);
            totalAmount += orderItem.getTotalPrice().longValue();
        }
        return totalAmount;
    }
}
