package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.PaymentRequest;
import com.electricity.electricity_billing_backend.enums.PaymentMethod;
import com.electricity.electricity_billing_backend.service.PaymentService;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;

@RestController
@RequestMapping("/api/webhook")
@RequiredArgsConstructor
@Slf4j
public class StripeWebhookController {

    private final PaymentService paymentService;

    @Value("${stripe.webhook.secret}")
    private String endpointSecret;

    @PostMapping("/stripe")
    public ResponseEntity<String> handleStripeWebhook(
            @RequestBody String payload,
            @RequestHeader("Stripe-Signature") String sigHeader) {

        Event event;

        try {
            event = Webhook.constructEvent(payload, sigHeader, endpointSecret);
        } catch (SignatureVerificationException e) {
            log.error("Stripe signature verification failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid signature");
        } catch (Exception e) {
            log.error("Webhook processing error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid payload");
        }

        if ("checkout.session.completed".equals(event.getType())) {
            Session session = (Session) event.getData().getObject();

            try {
                String billIdStr = session.getMetadata().get("billId");
                if (billIdStr != null) {
                    Long billId = Long.parseLong(billIdStr);
                    BigDecimal amount = new BigDecimal(session.getAmountTotal()).divide(new BigDecimal(100));
                    
                    PaymentRequest request = new PaymentRequest();
                    request.setBillId(billId);
                    request.setAmount(amount);
                    request.setPaymentMethod(PaymentMethod.CARD); // Defaulting to CARD for Stripe
                    request.setRemarks("Stripe Payment Session ID: " + session.getId());

                    // This will also mark the bill as PAID inside the service logic
                    paymentService.makePayment(request);
                    log.info("Successfully processed Stripe payment for Bill ID: {}", billId);
                }
            } catch (Exception e) {
                log.error("Error fulfilling Stripe order: ", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Fulfillment error");
            }
        }

        return ResponseEntity.ok("Success");
    }
}
