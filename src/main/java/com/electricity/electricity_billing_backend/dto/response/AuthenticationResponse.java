package com.electricity.electricity_billing_backend.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class AuthenticationResponse {

    private String token;

    private String email;

    private String role;

    private String message;
}