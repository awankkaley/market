package com.viaje.market.service;

import com.viaje.market.api_key.ApiKeyConfiguration;
import com.viaje.market.util.HmacValidator;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class SignatureServiceImpl implements SignatureService {
    private final ApiKeyConfiguration apiKeyConfiguration;

    @Override
    public boolean isValidSignature(String payload, String verify) {
        if (verify == null || payload == null || payload.equals("") || verify.equals("") || !HmacValidator.HashIsValid(apiKeyConfiguration.getSecretKey(), payload, verify)) {
            throw new IllegalArgumentException("Signature Failed");
        } else {
            return true;
        }
    }
}
