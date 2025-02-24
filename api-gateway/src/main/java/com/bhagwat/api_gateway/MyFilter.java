package com.bhagwat.api_gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.util.List;
import java.util.Set;

@Component
public class MyFilter implements GlobalFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        HttpHeaders headers = exchange.getRequest().getHeaders();  // ✅ No need to cast
        Set<String> keySet = headers.keySet();

        for (String key : keySet) {
            List<String> values = headers.get(key);
            System.out.println(key + " :: " + values);
        }

        return chain.filter(exchange);
    }
}
