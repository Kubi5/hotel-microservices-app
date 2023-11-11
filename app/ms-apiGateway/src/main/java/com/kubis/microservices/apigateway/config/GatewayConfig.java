package com.kubis.microservices.apigateway.config;

import com.kubis.microservices.apigateway.filter.JwtAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@RequiredArgsConstructor
public class GatewayConfig {

    private final JwtAuthenticationFilter filter;

    @Bean
    public RouteLocator routes(RouteLocatorBuilder builder) {
        return builder.routes()
                .route("register", r -> r.path("/register").filters(f -> f.filter(filter)).uri("lb://ms-customers"))
                .route("login", r -> r.path("/login").filters(f -> f.filter(filter)).uri("lb://ms-customers"))
                .route("customers", r -> r.path("/customers/**").filters(f -> f.filter(filter)).uri("lb://ms-customers"))
                .route("rooms", r -> r.path("/rooms/**").filters(f -> f.filter(filter)).uri("lb://ms-rooms"))
                .route("reservations", r -> r.path("/reservations/**").filters(f -> f.filter(filter)).uri("lb://ms-reservations"))
                .build();
    }

}
