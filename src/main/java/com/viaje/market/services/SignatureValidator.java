package com.viaje.market.services;

import com.viaje.market.config.api_key.ApiKeyConfiguration;
import com.viaje.market.util.HmacValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignatureValidator {
    private final ApiKeyConfiguration apiKeyConfiguration;

    public boolean isValidSignature(String payload, String verify) {
        if (verify == null || payload == null || payload.equals("") || verify.equals("") || !HmacValidator.HashIsValid(apiKeyConfiguration.getSecretKey(), payload, verify)) {
            throw new IllegalArgumentException("Signature Failed");
        } else {
            return true;
        }
    }
}
