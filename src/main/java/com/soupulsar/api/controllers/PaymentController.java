package com.soupulsar.api.controllers;

import com.soupulsar.application.dto.request.AsaasWebhookRequest;
import com.soupulsar.application.dto.request.CreatePaymentRequest;
import com.soupulsar.application.dto.response.PaymentProcessedResponse;
import com.soupulsar.application.usecase.payment.CreatePaymentUseCase;
import com.soupulsar.application.usecase.payment.HandlePaymentWebhookUseCase;
import com.soupulsar.application.usecase.payment.ProcessPaymentUseCase;
import com.soupulsar.domain.model.enums.GatewayPaymentEvent;
import com.soupulsar.infrastructure.gateway.asaas.AsaasEventMapper;
import com.soupulsar.infrastructure.gateway.asaas.AsaasWebhookSignatureValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/payments")
@RequiredArgsConstructor
public class PaymentController {

    private final AsaasWebhookSignatureValidator signatureValidator;
    private final AsaasEventMapper eventMapper;
    private final HandlePaymentWebhookUseCase  handlePaymentWebhookUseCase;
    private final CreatePaymentUseCase createPaymentUseCase;
    private final ProcessPaymentUseCase processPaymentUseCase;

    @PostMapping
    public ResponseEntity<String> createPayment(@RequestBody CreatePaymentRequest request) {

        var response = createPaymentUseCase.execute(request);
        return ResponseEntity.ok(response.toString());
    }

    @PostMapping("/process/{paymentId}")
    public ResponseEntity<PaymentProcessedResponse> processPayment(@PathVariable UUID paymentId) {

        var response = processPaymentUseCase.execute(paymentId);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/webhook")
    public ResponseEntity<Void> handlePaymentWebhook(@RequestHeader("asaas-access-token") String token, @RequestBody AsaasWebhookRequest request) {

        if (!signatureValidator.isValid(token)){
            return ResponseEntity.status(401).build();
        }

        GatewayPaymentEvent event = eventMapper.map(request.event());

        if (event == GatewayPaymentEvent.IGNORE){
            return ResponseEntity.ok().build();
        }

        handlePaymentWebhookUseCase.execute(request.id(), request.payment().id(), event);

        return ResponseEntity.ok().build();
    }
}