package com.electricity.electricity_billing_backend.service.impl;

import com.electricity.electricity_billing_backend.service.EmailService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class EmailServiceImpl implements EmailService {


    private final JavaMailSender mailSender;




    @Override
    public void sendEmail(
            String to,
            String subject,
            String message
    ) {

        SimpleMailMessage mail = new SimpleMailMessage();

        mail.setTo(to);
        mail.setSubject(subject);
        mail.setText(message);

        try {

    mailSender.send(mail);

    log.info("Email sent successfully to {}", to);

} catch (Exception ex) {

    log.error("Failed to send email to {} : {}", to, ex.getMessage());

    
}
    }

}
