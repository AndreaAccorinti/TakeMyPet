package com.takemypet.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Sends transactional emails (e.g., account unlock codes).
 */
@Service
public class EmailService {

    private static final Logger log = LoggerFactory.getLogger(EmailService.class);

    private final JavaMailSender mailSender;

    public EmailService(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Async
    public void sendAccountUnlockCode(String toAddress, String unlockCode) {
        sendEmail(
                toAddress,
                "TakeMyPet – Account Unlock Code",
                "Your account has been locked due to too many failed login attempts.\n\n" +
                "Use this code to unlock it: " + unlockCode + "\n\n" +
                "If this wasn't you, please contact support.");
    }

    private void sendEmail(String toAddress, String subject, String body) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setTo(toAddress);
            message.setSubject(subject);
            message.setText(body);
            mailSender.send(message);
            log.info("Email sent to {}", toAddress);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", toAddress, e.getMessage());
        }
    }
}
