package com.electricity.electricity_billing_backend.service.impl;

import com.electricity.electricity_billing_backend.dto.request.LoginRequest;
import com.electricity.electricity_billing_backend.dto.request.RegisterRequest;
import com.electricity.electricity_billing_backend.dto.response.AuthenticationResponse;
import com.electricity.electricity_billing_backend.entity.Role;
import com.electricity.electricity_billing_backend.entity.User;
import com.electricity.electricity_billing_backend.enums.RoleType;
import com.electricity.electricity_billing_backend.exception.DuplicateResourceException;
import com.electricity.electricity_billing_backend.repository.RoleRepository;
import com.electricity.electricity_billing_backend.repository.UserRepository;
import com.electricity.electricity_billing_backend.repository.PasswordResetTokenRepository;
import com.electricity.electricity_billing_backend.entity.PasswordResetToken;
import com.electricity.electricity_billing_backend.security.JwtService;
import com.electricity.electricity_billing_backend.service.AuthenticationService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.electricity.electricity_billing_backend.service.EmailService;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthenticationServiceImpl implements AuthenticationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordResetTokenRepository passwordResetTokenRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final EmailService emailService;


    @Override
    public AuthenticationResponse register(RegisterRequest request) {

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("Email already exists.");
        }

        if (userRepository.existsByMobileNumber(request.getMobileNumber())) {
            throw new DuplicateResourceException("Mobile number already exists.");
        }

        Role role = roleRepository.findByName(request.getRole())
                .orElseThrow(() -> new RuntimeException("Role not found."));

        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .mobileNumber(request.getMobileNumber())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .build();

        User savedUser = userRepository.save(user);

        log.info("New user registered: {}", savedUser.getEmail());

       try {

    emailService.sendEmail(
            savedUser.getEmail(),
            "Welcome to Electricity Billing System",
            """
            Dear %s,

            Welcome to Electricity Billing Management System.

            Your account has been created successfully.

            Email : %s

            Role : %s

            Thank you.
            """
                    .formatted(
                            savedUser.getFullName(),
                            savedUser.getEmail(),
                            savedUser.getRole().getName()
                    )
    );

    log.info("Welcome email sent to {}", savedUser.getEmail());

} catch (Exception ex) {

    log.error("Email sending failed: {}", ex.getMessage());

}

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        savedUser.getEmail(),
                        savedUser.getPassword(),
                        java.util.List.of(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        savedUser.getRole().getName().name()
                                )
                        )
                )
        );



        return AuthenticationResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .message("Registration Successful")
                .build();
    }

    @Override
    public AuthenticationResponse login(LoginRequest request) {

        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

        } catch (Exception ex) {

            log.warn("Failed login attempt: {}", request.getEmail());

            throw ex;
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found."));

        log.info("User logged in successfully: {}", user.getEmail());

        String token = jwtService.generateToken(
                new org.springframework.security.core.userdetails.User(
                        user.getEmail(),
                        user.getPassword(),
                        java.util.List.of(
                                new org.springframework.security.core.authority.SimpleGrantedAuthority(
                                        user.getRole().getName().name()
                                )
                        )
                )
        );



        return AuthenticationResponse.builder()
                .token(token)
                .email(user.getEmail())
                .role(user.getRole().getName().name())
                .message("Login Successful")
                .build();
    }

    @Override
    public void forgotPassword(com.electricity.electricity_billing_backend.dto.request.ForgotPasswordRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("User with this email does not exist."));

        // Delete any existing tokens for this user
        passwordResetTokenRepository.deleteByUser(user);

        // Generate token
        String token = java.util.UUID.randomUUID().toString();
        PasswordResetToken resetToken = PasswordResetToken.builder()
                .token(token)
                .user(user)
                .expiryDate(java.time.LocalDateTime.now().plusHours(1))
                .build();

        passwordResetTokenRepository.save(resetToken);

        String frontendUrl = System.getenv().getOrDefault("FRONTEND_URL", "http://localhost:4200");
        String resetLink = frontendUrl + "/reset-password?token=" + token;

        try {
            emailService.sendEmail(
                    user.getEmail(),
                    "Password Reset Request",
                    "Click the link to reset your password (valid for 1 hour):\n\n" + resetLink
            );
            log.info("Password reset email sent to {}", user.getEmail());
        } catch (Exception ex) {
            log.error("Failed to send reset email", ex);
            throw new RuntimeException("Failed to send reset email");
        }
    }

    @Override
    public void resetPassword(com.electricity.electricity_billing_backend.dto.request.ResetPasswordRequest request) {
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(request.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid or expired token."));

        if (resetToken.getExpiryDate().isBefore(java.time.LocalDateTime.now())) {
            passwordResetTokenRepository.delete(resetToken);
            throw new RuntimeException("Token has expired.");
        }

        User user = resetToken.getUser();
        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);

        passwordResetTokenRepository.delete(resetToken);
        log.info("Password successfully reset for user: {}", user.getEmail());
    }
}
