package com.electricity.electricity_billing_backend.service.impl;

import com.electricity.electricity_billing_backend.dto.request.ChangePasswordRequest;
import com.electricity.electricity_billing_backend.dto.request.UpdateProfileRequest;
import com.electricity.electricity_billing_backend.dto.response.ProfileResponse;
import com.electricity.electricity_billing_backend.entity.User;
import com.electricity.electricity_billing_backend.exception.ResourceNotFoundException;
import com.electricity.electricity_billing_backend.repository.UserRepository;
import com.electricity.electricity_billing_backend.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileServiceImpl implements ProfileService {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    @Override
    public ProfileResponse getProfile() {

        User user = getCurrentUser();

        return mapToResponse(user);

    }

    @Override
    public ProfileResponse updateProfile(UpdateProfileRequest request) {

        User user = getCurrentUser();

        user.setFullName(request.getFullName());

        user.setMobileNumber(request.getMobileNumber());

        userRepository.save(user);

        return mapToResponse(user);

    }

    private User getCurrentUser() {

        Authentication authentication =
                SecurityContextHolder
                        .getContext()
                        .getAuthentication();

        if (authentication == null) {
            throw new RuntimeException("User not authenticated");
        }

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new ResourceNotFoundException("User not found"));

    }

    private ProfileResponse mapToResponse(User user) {

        return ProfileResponse.builder()

                .id(user.getId())

                .fullName(user.getFullName())

                .email(user.getEmail())

                .mobileNumber(user.getMobileNumber())

                .role(user.getRole().getName().name())

                .build();

    }

    @Override
    public void changePassword(ChangePasswordRequest request) {

        User user = getCurrentUser();

        if (!passwordEncoder.matches(
                request.getCurrentPassword(),
                user.getPassword()
        )) {

            throw new RuntimeException("Current password is incorrect.");

        }

        user.setPassword(

                passwordEncoder.encode(
                        request.getNewPassword()
                )

        );

        userRepository.save(user);

    }

}