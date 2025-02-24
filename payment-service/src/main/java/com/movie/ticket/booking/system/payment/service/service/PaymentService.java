package com.movie.ticket.booking.system.payment.service.service;

import com.movie.ticket.booking.system.payment.service.dto.BookingDTO;

public interface PaymentService {

    public BookingDTO makePayment(BookingDTO bookingDTO);
}
