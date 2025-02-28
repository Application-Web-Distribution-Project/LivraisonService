package com.restaurant.reclamations.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.reactive.CorsWebFilter;
import org.springframework.web.cors.reactive.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
public class WebConfig {
    @Bean
    public CorsWebFilter corsWebFilter() {
        CorsConfiguration corsConfig = new CorsConfiguration();

        // Autoriser l'accès depuis l'API Gateway et Angular
        corsConfig.setAllowedOrigins(List.of(
                "http://angular-frontend:4200", // ✅ Frontend dans Docker
                "http://localhost:4200",        // ✅ Frontend en local
                "http://api-gateway:8081",      // ✅ Gateway en Docker
                "http://localhost:8081"         // ✅ Gateway en local
        ));

        corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        corsConfig.setAllowedHeaders(List.of("*"));
        corsConfig.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfig);

        return new CorsWebFilter(source);
    }
}
