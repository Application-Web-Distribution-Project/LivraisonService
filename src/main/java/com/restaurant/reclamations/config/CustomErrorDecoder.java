package com.restaurant.reclamations.config;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorDecoder implements ErrorDecoder {

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == 500) {
            // Handle internal server errors
            return new RuntimeException("Internal Server Error");
        }
        return new Exception("Generic error");
    }
}
