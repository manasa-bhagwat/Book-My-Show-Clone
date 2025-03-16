//package com.bhagwat.api_gateway;
//
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import org.springframework.web.server.WebFilter;
//import org.springframework.web.server.WebFilterChain;
//import reactor.core.publisher.Mono;
//
//import java.util.HashSet;
//import java.util.Set;
//
//@Component
//public class CustomAuthorizationFilter implements WebFilter {
//
//    private final Set<String> allowList = new HashSet<>();
//    private final Set<String> denyList = new HashSet<>();
//
//    public CustomAuthorizationFilter() {
//        // Add allowed IPs
//        allowList.add("192.168.1.100");
//        allowList.add("127.0.0.1");
//
//        // Add denied IPs
//        denyList.add("192.168.1.101");
//        denyList.add("10.0.0.5");
//    }
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String clientIp = request.getRemoteAddress().getAddress().getHostAddress();
//
//        // ✅ Check deny list
//        if (denyList.contains(clientIp)) {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.FORBIDDEN);
//            return response.setComplete();
//        }
//
//        // ✅ Check allow list (if needed)
//        if (!allowList.contains(clientIp)) {
//            ServerHttpResponse response = exchange.getResponse();
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            return response.setComplete();
//        }
//
//        return chain.filter(exchange);
//    }
//
//}
//
