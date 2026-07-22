package com.electricity.electricity_billing_backend.dto.response;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProfileResponse {

    private Long id;

    private String fullName;

    private String email;

    private String mobileNumber;

    private String role;

    private String consumerNumber;

}