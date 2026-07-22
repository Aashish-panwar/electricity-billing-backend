package com.electricity.electricity_billing_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MonthlyBillsResponse {

    private String month;

    private Long bills;

}