package com.movie.ticket.booking.system.payment.service.service;

import com.movie.ticket.booking.system.payment.service.dto.BookingDTO;
import com.movie.ticket.booking.system.payment.service.enums.BookingStatus;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.Charge;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@Slf4j
public class StripeApiPaymentGateway {

    @Value("${stripe.secret.key}")
    private String secretKey;

    @PostConstruct
    public void init() {
        Stripe.apiKey = secretKey;
    }

    public BookingDTO makePayment(BookingDTO bookingDTO) {
        Map<String, Object> chargeParams = new HashMap<>();
        chargeParams.put("amount", (int) (bookingDTO.getBookingAmount() * 100));
        chargeParams.put("currency", "inr");
        chargeParams.put("description", "Test payment for booking service.");
        chargeParams.put("source", "tok_in");

        try {
            Charge.create(chargeParams);
            bookingDTO.setBookingStatus(BookingStatus.CONFIRMED);
        } catch (StripeException e) {
            log.error("Error encountered during payment." + e.getMessage());
            bookingDTO.setBookingStatus(BookingStatus.CANCELLED);
        }

        return bookingDTO;
    }

}
