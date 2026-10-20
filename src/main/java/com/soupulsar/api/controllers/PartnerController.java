package com.soupulsar.api.controllers;

import com.soupulsar.application.partner.*;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/partners")
@RequiredArgsConstructor
@Tag(name = "Partners", description = "Endpoints for managing partners")
public class PartnerController {

    private final RequestPartnerDiscountCodeUseCase requestPartnerDiscountCodeUseCase;
    private final RedeemPartnerDiscountCodeUseCase redeemPartnerDiscountCodeUseCase;
    private final CreatePartnerDiscountUseCase createPartnerDiscountUseCase;
    private final CreatePartnerUseCase createPartnerUseCase;
    private final CreatePartnerEligibilityUseCase createPartnerEligibilityUseCase;

    @PostMapping("/request-code")
    public ResponseEntity<PartnerDiscountCodeResponse> requestDiscountCode(@RequestBody PartnerDiscountCodeRequest request) {
        requestPartnerDiscountCodeUseCase.execute(request);
        return ResponseEntity.ok(new PartnerDiscountCodeResponse("If you are eligible, you will receive a discount code via email."));
    }

    @PostMapping("/redeem-code")
    public ResponseEntity<Void> redeemDiscountCode(@RequestBody RedeemPartnerDiscountCodeRequest request) {
        redeemPartnerDiscountCodeUseCase.execute(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<Void> createPartner(@RequestBody CreatePartnerRequest request) {
        createPartnerUseCase.execute(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/discounts")
    public ResponseEntity<Void> createPartnerDiscount(@RequestBody CreatePartnerDiscountRequest request) {
        createPartnerDiscountUseCase.execute(request);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/eligibility")
    public ResponseEntity<Void> createPartnerEligibility(@RequestBody CreatePartnerEligibilityRequest request) {
        createPartnerEligibilityUseCase.execute(request);
        return ResponseEntity.ok().build();
    }
}