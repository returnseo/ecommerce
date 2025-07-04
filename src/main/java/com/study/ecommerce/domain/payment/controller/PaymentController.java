package com.study.ecommerce.domain.payment.controller;

import com.study.ecommerce.domain.payment.dto.PaymentRequest;
import com.study.ecommerce.domain.payment.dto.PaymentResult;
import com.study.ecommerce.domain.payment.service.PaymentService;
import com.study.ecommerce.infra.payment.dto.PaymentGatewayRequest;
import com.study.ecommerce.infra.payment.dto.PaymentGatewayResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Payment", description = "결제 API")
@RestController
@RequestMapping("/api/v1/payments")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @Operation(summary = "Factory Pattern 결제 처리", description = "Factory Pattern을 사용하여 결제를 처리합니다.")
    @PostMapping("/factory")
    public ResponseEntity<PaymentResult> processPaymentWithFactory(@RequestBody PaymentRequest request) {
        return ResponseEntity.ok(paymentService.processPaymentWithFactory(request));
    }

    @Operation(summary = "Adapter Pattern 결제 처리", description = "Adapter Pattern 을 사용하여 외부 결제 게이트웨이로 결제를 처리한다.")
    @PostMapping("/gateway/{gatewayName}")
    public ResponseEntity<PaymentGatewayResponse> processPaymentWithAdapter(
            @PathVariable String gatewayName,
            @RequestBody PaymentGatewayRequest request
            ) {
        return ResponseEntity.ok(paymentService.processPaymentWithAdapter(gatewayName, request));
    }

    @Operation(summary = "사용 가능한 결제 방식 조회", description = "Factory Pattern 으로 관리되는 결제 방식 목록을 조회한다.")
    @GetMapping("/methods")
    public ResponseEntity<List<String>> getAvailablePaymentMethods() {
        return ResponseEntity.ok(paymentService.getAvailablePaymentMethods());
    }

    @Operation(summary = "사용 가능한 결제 게이트웨이 조회", description = "Adapter Pattern 으로 관리되는 결제 게이트웨이 목록을 조회한다.")
    @GetMapping("/gateways")
    public ResponseEntity<List<String>> getAvailablePaymentGateways() {
        return ResponseEntity.ok(paymentService.getAvailablePaymentGateways());
    }
}
