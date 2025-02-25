package com.movie.ticket.booking.system.service.kafka.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.movie.ticket.booking.system.service.dto.BookingDTO;
import com.movie.ticket.booking.system.service.service.BookingService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookingServiceKafkaListener {

    private final ObjectMapper objectMapper;
    private final BookingService bookingService;

    @KafkaListener(topics = "payment-response", groupId = "payment-response-group1")
    public void pullBookingDetailsFromPaymentResponseTopic(String bookingsJson){
        log.info("Received booking details {} from payment-response topic", bookingsJson);
        try{
            this.bookingService.processFinalBooking(objectMapper.readValue(bookingsJson, BookingDTO.class));
        } catch (Exception e) {
            log.info("Error while processing booking details {} from payment-response topic", bookingsJson);
        }
    }
}
