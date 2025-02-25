package com.movie.ticket.booking.system.service.kafka.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.booking.system.service.dto.BookingDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceKafkaPublisher {

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void pushBookingDetailsToPaymentRequestTopic(BookingDTO bookingDTO) {
        log.info("Publishing booking request to payment-request topic.");
        try {
            this.kafkaTemplate.send("payment-request", objectMapper.writeValueAsString(bookingDTO));
        } catch (Exception e) {
            log.info("Error while publishing booking request to payment-request topic.");
        }
    }
}
