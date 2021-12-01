package com.viaje.market.util;

import com.viaje.market.config.api_key.ApiKeyConfiguration;
import com.viaje.market.dtos.CoinsbitSignature;
import lombok.AllArgsConstructor;

import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

import static sun.security.pkcs11.wrapper.Functions.toHexString;

public class HmacValidator {


    public static String ComputeHash(String secret, String payload)
            throws InvalidKeyException, NoSuchAlgorithmException {
        String digest = "HmacSHA256";
        Mac mac = Mac.getInstance(digest);
        mac.init(new SecretKeySpec(secret.getBytes(), digest));
        return new String(Base64.getEncoder().encode(mac.doFinal(payload.getBytes())));
    }

    public static boolean HashIsValid(String secret, String payload, String verify) {
        try {
            String computedHash = ComputeHash(secret, payload);
            return MessageDigest.isEqual(computedHash.getBytes(StandardCharsets.UTF_8),
                    verify.getBytes(StandardCharsets.UTF_8));
        } catch (Exception ex) {
            throw new IllegalArgumentException("Signature Failed");
        }
    }

    public static String generateSignature(String secret, String data) {
        try {
            Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
            SecretKeySpec secret_key = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
            sha256_HMAC.init(secret_key);
            return Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(data.getBytes()));

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}

