package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.entity.Bill;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.repository.BillRepository;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.checkout.Session;
import com.stripe.param.checkout.SessionCreateParams;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class StripeService {

    @Value("${stripe.secret.key}")
    private String stripeSecretKey;

    @Value("${FRONTEND_URL:http://localhost:4200}")
    private String frontendUrl;

    private final BillRepository billRepository;

    @PostConstruct
    public void init() {
        Stripe.apiKey = stripeSecretKey;
    }

    public String createCheckoutSession(Long billId) {
        Bill bill = billRepository.findById(billId)
                .orElseThrow(() -> new ResourceNotFoundException("Bill not found with id: " + billId));

        if (bill.getStatus().name().equals("PAID")) {
            throw new RuntimeException("Bill is already paid.");
        }

        long amountInCents = bill.getTotalAmount().multiply(new BigDecimal(100)).longValue();

        SessionCreateParams params = SessionCreateParams.builder()
                .setMode(SessionCreateParams.Mode.PAYMENT)
                .setSuccessUrl(frontendUrl + "/payments/success?session_id={CHECKOUT_SESSION_ID}&bill_id=" + billId)
                .setCancelUrl(frontendUrl + "/payments/cancel")
                .putMetadata("billId", String.valueOf(billId))
                .putMetadata("consumerId", String.valueOf(bill.getConsumer().getId()))
                .addLineItem(
                        SessionCreateParams.LineItem.builder()
                                .setQuantity(1L)
                                .setPriceData(
                                        SessionCreateParams.LineItem.PriceData.builder()
                                                .setCurrency("usd") // Change this to INR or your currency if needed
                                                .setUnitAmount(amountInCents)
                                                .setProductData(
                                                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                                                                .setName("Electricity Bill - " + bill.getBillNumber())
                                                                .build()
                                                )
                                                .build()
                                )
                                .build()
                )
                .build();

        try {
            Session session = Session.create(params);
            return session.getUrl();
        } catch (StripeException e) {
            log.error("Error creating Stripe checkout session: ", e);
            throw new RuntimeException("Could not initiate payment session.");
        }
    }
}
