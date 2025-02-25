package com.movie.ticket.booking.system.payment.service.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.booking.system.payment.service.dto.BookingDTO;
import com.movie.ticket.booking.system.payment.service.service.PaymentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentServiceKafkaListener {

    private final ObjectMapper objectMapper;
    private final PaymentService paymentService;

    @KafkaListener(topics = "payment-request", groupId = "payment-request-group1")
    public void receivedFromDataFromPaymentRequestTopic(String bookingsDetailsJson) {
        log.info("Received booking details {} from payment request topic.", bookingsDetailsJson);
        try {
            this.paymentService.processPayment(objectMapper.readValue(bookingsDetailsJson, BookingDTO.class));
        } catch (Exception e) {
            log.info("Error while receiving booking details {} from payment request topic.", bookingsDetailsJson);
        }
    }
}
