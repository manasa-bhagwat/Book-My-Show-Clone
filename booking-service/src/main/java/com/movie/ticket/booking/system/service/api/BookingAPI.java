package com.movie.ticket.booking.system.service.api;

import com.movie.ticket.booking.system.service.dto.BookingDTO;
import com.movie.ticket.booking.system.service.service.impl.BookingServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("bookings")
@Slf4j
public class BookingAPI {

    @Autowired
    BookingServiceImpl bookingService;

    @PostMapping
    public BookingDTO createBooking(@Valid @RequestBody BookingDTO bookingDTO){
        log.info("Entered into Controller:BookingApi createBooking method with request data: {}", bookingDTO);

        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        log.info("Response from Service Layer createBooking method with response data : {}", createdBooking);

        return createdBooking;
    }

}
