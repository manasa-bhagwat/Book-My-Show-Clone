package com.movie.ticket.booking.system.service.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.ticket.booking.system.service.dto.BookingDTO;
import com.movie.ticket.booking.system.service.exception.BookingsException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface BookingService {

    public BookingDTO createBooking(BookingDTO bookingDTO) throws JsonProcessingException;
    public BookingDTO getBookingDetails(UUID bookingDTO) throws BookingsException;
    public void processFinalBooking(BookingDTO bookingDTO) throws BookingsException;

    public Page<BookingDTO> getAllBookings(Pageable pageable);

}
