package com.arnoldgalovics.online.store.service.external.error;

import com.arnoldgalovics.online.store.service.api.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class InventoryServiceErrorDecoder extends ErrorDecoder.Default {
    private ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        if (response.status() == HttpStatus.BAD_REQUEST.value()) {
            try {
                String errorRespStr = new String(response.body().asInputStream().readAllBytes(), StandardCharsets.UTF_8);
                ErrorResponse errorResponse = objectMapper.readValue(errorRespStr, ErrorResponse.class);
                return new ProductCreationFailedException("Product creation failed due to error: " + errorResponse.getMessage());
            } catch (IOException e) {
                throw new RuntimeException("Error while decoding the error", e);
            }
        }
        return super.decode(methodKey, response);
    }
}
