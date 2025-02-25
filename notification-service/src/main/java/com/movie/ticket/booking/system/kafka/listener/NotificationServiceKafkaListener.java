package com.movie.ticket.booking.system.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.booking.system.email.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceKafkaListener {

    private final ObjectMapper objectMapper;
    private final EmailService emailService;

    @KafkaListener(topics = "payment-response", groupId = "payment-response-group2")
    public void pullBookingDetailsFromPaymentResponseTopic(String bookingsJson) {
        log.info("Received booking details {} from payment-response topic", bookingsJson);
        try {
            //send email logic
            String recipientEmail = "maansaflaunts@gmail.com";
            String subject = "Booking confirmation";
            String messageBody = "Your booking is confirmed! Details: " + bookingsJson;

            emailService.sendEmail(recipientEmail, subject, messageBody);
        } catch (Exception e) {
            log.error("Error while sending email for booking details {} ", bookingsJson, e);
        }
    }
}
