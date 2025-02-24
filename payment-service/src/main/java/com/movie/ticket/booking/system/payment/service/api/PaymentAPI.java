package com.movie.ticket.booking.system.payment.service.api;

import com.movie.ticket.booking.system.payment.service.dto.BookingDTO;
import com.movie.ticket.booking.system.payment.service.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("payments")
public class PaymentAPI {

    @Autowired
    private PaymentService paymentService;

    @PostMapping
    public BookingDTO makePayment(@RequestBody BookingDTO bookingDTO) {
        return this.paymentService.makePayment(bookingDTO);
    }
}
