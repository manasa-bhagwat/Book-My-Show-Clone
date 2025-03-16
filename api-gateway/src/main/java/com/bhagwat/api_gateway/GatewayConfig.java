package com.bhagwat.api_gateway;

import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.handler.SimpleUrlHandlerMapping;
import reactor.core.publisher.Mono;

import java.util.Collections;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder, ParameterValidationFilter parameterValidationFilter) {
        return builder.routes()
                .route("booking-service", r -> r.path("/v1/bookings/**")
                        .filters(f -> f
                                .filter(new ParameterValidationFilter())
                                .retry(config -> config
                                        .setRetries(3)
                                        .setMethods(HttpMethod.GET, HttpMethod.POST, HttpMethod.DELETE)
                                )
                                .circuitBreaker(config -> config
                                        .setName("bookingService")
                                        .setFallbackUri("forward:/fallback/bookings")
                                )
                        )
                        .uri("lb://BOOKING-SERVICE")
                )
                .build();
    }

}
