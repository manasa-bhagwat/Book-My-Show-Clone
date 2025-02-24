package com.bhagwat.api_gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookingAPIHandler {

    @GetMapping("/booking-fallback")
    public String bookingApiFallback() {
        return "Booking service is in maintenance. Please try after sometime.";
    }
}