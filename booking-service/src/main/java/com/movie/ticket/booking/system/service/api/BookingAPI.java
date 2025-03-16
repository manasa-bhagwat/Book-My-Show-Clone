package com.movie.ticket.booking.system.service.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.movie.ticket.booking.system.service.dto.BookingDTO;
import com.movie.ticket.booking.system.service.exception.BookingsException;
import com.movie.ticket.booking.system.service.service.impl.BookingServiceImpl;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/v1/bookings")
@Slf4j
public class BookingAPI {

    private final BookingServiceImpl bookingService;
    private final CacheManager cacheManager;

    public BookingAPI(BookingServiceImpl bookingService, CacheManager cacheManager) {
        this.bookingService = bookingService;
        this.cacheManager = cacheManager;
    }


    @PostMapping
    public BookingDTO createBooking(@Valid @RequestBody BookingDTO bookingDTO) throws JsonProcessingException {
        log.info("Entered into Controller: BookingApi createBooking method with request data: {}", bookingDTO);

        BookingDTO createdBooking = bookingService.createBooking(bookingDTO);
        log.info("Response from Service Layer createBooking method with response data : {}", createdBooking);

        return createdBooking;
    }

    @GetMapping("/{bookingId}")
    public BookingDTO getBookingDetails(@PathVariable UUID bookingId) throws BookingsException {
        log.info("Control Layer: Fetching booking details for ID: {}", bookingId);

        return this.bookingService.getBookingDetails(bookingId);
    }

    @GetMapping
    public ResponseEntity<Page<BookingDTO>> getAllBookings(Pageable pageable) {
        log.info("Control Layer: Fetching all booking details..... ");

        Page<BookingDTO> bookings = bookingService.getAllBookings(pageable);

        return ResponseEntity.ok(bookings);
    }

    @DeleteMapping("/clear-cache")
    public String clearCache() {
        Objects.requireNonNull(cacheManager.getCache("bookings")).clear();
        return "Cache cleared!";
    }

    @GetMapping("/view-cache")
    public Object viewCache() {
        Cache cache = cacheManager.getCache("bookings");
        if (cache != null) {
            Map<Object, Object> nativeCache = (Map<Object, Object>) cache.getNativeCache();
            return nativeCache;
        }
        return "Cache is empty";
    }


}
