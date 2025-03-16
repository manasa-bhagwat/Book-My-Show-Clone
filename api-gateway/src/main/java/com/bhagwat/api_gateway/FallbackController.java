package com.bhagwat.api_gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/fallback")
public class FallbackController {

    @GetMapping("/bookings")
    public Mono<String> bookingFallback() {
        return Mono.just("Booking Service is currently unavailable for GET requests. Please try again later.");
    }

    @PostMapping("/bookings")
    public Mono<String> postFallback() {
        return Mono.just("Booking Service is currently unavailable for POST requests. Please try again later.");
    }
}

