package com.electricity.electricity_billing_backend.controller;


import com.electricity.electricity_billing_backend.dto.request.LoginRequest;
import com.electricity.electricity_billing_backend.dto.request.RegisterRequest;
import com.electricity.electricity_billing_backend.dto.response.AuthenticationResponse;
import com.electricity.electricity_billing_backend.service.AuthenticationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    @Operation(summary = "Register User")
    @Tag(name = "Authentication")
    public ResponseEntity<AuthenticationResponse> register(
            @Valid @RequestBody RegisterRequest request) {

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authenticationService.register(request));
    }

    @PostMapping("/login")
    @Operation(summary = "Login User")
    @Tag(name = "Authentication")
    public ResponseEntity<AuthenticationResponse> login(
            @Valid @RequestBody LoginRequest request) {

        return ResponseEntity.ok(authenticationService.login(request));
    }
}