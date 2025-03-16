//package com.bhagwat.api_gateway;
//
//import org.springframework.cloud.gateway.filter.GatewayFilter;
//import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.WebFilterChain;
//
//@Component
//public class AuthorizationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {
//
//    private final CustomAuthorizationFilter customAuthorizationFilter;
//
//    public AuthorizationGatewayFilterFactory(CustomAuthorizationFilter customAuthorizationFilter) {
//        this.customAuthorizationFilter = customAuthorizationFilter;
//    }
//
//    @Override
//    public GatewayFilter apply(Object config) {
//        return (exchange, chain) -> customAuthorizationFilter.filter(exchange, (WebFilterChain) chain);
//    }
//}
