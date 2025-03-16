package com.bhagwat.api_gateway;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Component
public class ParameterValidationFilter implements GatewayFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        String path = exchange.getRequest().getPath().value();
        Map<String, List<String>> queryParams = exchange.getRequest().getQueryParams();

        // Only validate UUID if the path indicates a bookingId is expected.
        // Example: /v1/bookings/{bookingId}
        if (path.startsWith("/v1/bookings/")) {
            String subPath = path.substring("/v1/bookings/".length());
            // Skip validation for known endpoints that don't represent a bookingId
            if (!subPath.equalsIgnoreCase("view-cache")) {
                // Try to parse the subPath as UUID; if it fails, return error
                try {
                    UUID.fromString(subPath);
                    log.info("Valid UUID format for bookingId: {}", subPath);
                } catch (IllegalArgumentException e) {
                    log.warn("Invalid UUID format for bookingId: {}", subPath);
                    return handleBadRequest(exchange, "Invalid bookingId format. Must be a valid UUID.");
                }
            }
        }
        // For the base /v1/bookings endpoint (without bookingId in the path), skip UUID validation.

        // Validate pagination parameters for /v1/bookings (only if query parameters are present)
        if (queryParams.containsKey("page")) {
            try {
                int page = Integer.parseInt(queryParams.get("page").get(0));
                if (page < 0) {
                    return handleBadRequest(exchange, "Page index must be greater than or equal to 0.");
                }
            } catch (NumberFormatException e) {
                return handleBadRequest(exchange, "Invalid page format. Must be a number.");
            }
        }

        if (queryParams.containsKey("size")) {
            try {
                int size = Integer.parseInt(queryParams.get("size").get(0));
                if (size <= 0 || size > 100) {
                    return handleBadRequest(exchange, "Size must be between 1 and 100.");
                }
            } catch (NumberFormatException e) {
                return handleBadRequest(exchange, "Invalid size format. Must be a number.");
            }
        }

        // Pass request to the next filter
        return chain.filter(exchange);
    }

    // Handle bad request response
    private Mono<Void> handleBadRequest(ServerWebExchange exchange, String errorMessage) {
        exchange.getResponse().setStatusCode(HttpStatus.BAD_REQUEST);
        exchange.getResponse().getHeaders().add("Content-Type", "application/json");
        String responseBody = String.format("{\"error\": \"%s\"}", errorMessage);
        return exchange.getResponse()
                .writeWith(Mono.just(exchange.getResponse()
                        .bufferFactory()
                        .wrap(responseBody.getBytes())));
    }

    @Override
    public int getOrder() {
        return -1; // High priority filter
    }
}
