package com.bhagwat.api_gateway;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PaymentAPIHandler {

    @GetMapping("/payment-fallback")
    public String paymentApiFallback() {
        return "Payment service is in maintenance. Please try after sometime.";
    }
}
