package com.movie.ticket.booking.system.email;

import jakarta.mail.internet.MimeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class EmailService {

    private final JavaMailSender javaMailSender;

    public EmailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void sendEmail(String recipientEmail, String subject, String htmlContent) {
        try {
            if (recipientEmail == null || recipientEmail.trim().isEmpty()) {
                log.error("Recipient email is null or empty, skipping email send.");
                return;
            }

            MimeMessage message = javaMailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true);

            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(htmlContent, true);

            javaMailSender.send(message);
            log.info("Email sent successfully to {}", recipientEmail);
        } catch (Exception e) {
            log.error("Failed to send email to {}: {}", recipientEmail, e.getMessage(), e);
        }
    }
}
