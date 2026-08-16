package com.electricity.electricity_billing_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RazorpayOrderResponse {
    private String orderId;
    private String amount;
    private String currency;
}
