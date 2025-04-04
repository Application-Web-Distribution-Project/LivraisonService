package com.restaurant.reclamations.config;

import feign.Logger;
import feign.Response;
import feign.codec.Decoder;
import feign.codec.ErrorDecoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringDecoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Configuration
public class FeignConfig {

    @Bean
    public Logger.Level feignLoggerLevel() {
        return Logger.Level.FULL; // Active les logs détaillés
    }

    @Bean
    public Decoder feignDecoder() {
        MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter();
        converter.setSupportedMediaTypes(List.of(MediaType.APPLICATION_JSON, MediaType.TEXT_HTML)); // Ajoute text/html

        return new SpringDecoder(() -> new HttpMessageConverters(converter));
    }

    @Bean
    public ErrorDecoder errorDecoder() {
        return new CustomErrorDecoder(); // Register the custom error decoder
    }

    @Component
    public class CustomErrorDecoder implements ErrorDecoder {

        @Override
        public Exception decode(String methodKey, Response response) {
            if (response.headers().get("Content-Type") != null && response.headers().get("Content-Type").contains("text/html")) {
                // Handle unexpected HTML responses
                return new RuntimeException("Received HTML response instead of JSON");
            }
            return new Exception("Generic error");
        }
    }
}

