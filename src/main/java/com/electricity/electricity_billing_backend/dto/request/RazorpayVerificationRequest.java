package com.electricity.electricity_billing_backend.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RazorpayVerificationRequest {
    private String razorpayPaymentId;
    private String razorpayOrderId;
    private String razorpaySignature;
}
