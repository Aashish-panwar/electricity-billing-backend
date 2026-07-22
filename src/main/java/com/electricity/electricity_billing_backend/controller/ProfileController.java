package com.electricity.electricity_billing_backend.controller;

import com.electricity.electricity_billing_backend.dto.request.ChangePasswordRequest;
import com.electricity.electricity_billing_backend.dto.request.UpdateProfileRequest;
import com.electricity.electricity_billing_backend.dto.response.ProfileResponse;
import com.electricity.electricity_billing_backend.service.ProfileService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProfileController {

    private final ProfileService profileService;

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> getProfile() {

        return ResponseEntity.ok(
                profileService.getProfile()
        );

    }

    @PutMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<ProfileResponse> updateProfile(
            @Valid @RequestBody UpdateProfileRequest request
    ) {

        return ResponseEntity.ok(
                profileService.updateProfile(request)
        );

    }

    @PutMapping("/change-password")
    public ResponseEntity<String> changePassword(
            @Valid @RequestBody ChangePasswordRequest request) {

        profileService.changePassword(request);

        return ResponseEntity.ok("Password changed successfully.");

    }

}