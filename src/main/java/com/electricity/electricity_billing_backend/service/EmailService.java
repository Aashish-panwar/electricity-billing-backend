package com.electricity.electricity_billing_backend.service;

public interface EmailService {

    void sendEmail(
            String to,
            String subject,
            String message
    );

}