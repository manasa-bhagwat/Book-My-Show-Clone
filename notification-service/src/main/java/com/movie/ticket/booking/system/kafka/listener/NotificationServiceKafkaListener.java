package com.movie.ticket.booking.system.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationServiceKafkaListener {

    private final ObjectMapper objectMapper;

    @KafkaListener(topics = "payment-response", groupId = "payment-response-group2")
    public void pullBookingDetailsFromPaymentResponseTopic(String bookingsJson) {
        log.info("Received booking details {} from payment-response topic", bookingsJson);
        try {
            //send email logic
        } catch (Exception e) {
            log.info("Error while sending email for booking details {} ", bookingsJson);
        }
    }
}
