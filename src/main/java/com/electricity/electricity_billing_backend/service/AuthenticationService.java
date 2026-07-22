package com.electricity.electricity_billing_backend.service;

import com.electricity.electricity_billing_backend.dto.request.LoginRequest;
import com.electricity.electricity_billing_backend.dto.request.RegisterRequest;
import com.electricity.electricity_billing_backend.dto.response.AuthenticationResponse;

public interface AuthenticationService {

    AuthenticationResponse register(RegisterRequest request);

    AuthenticationResponse login(LoginRequest request);
}