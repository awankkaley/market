package com.viaje.market;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacValidator
{
    public static String ComputeHash(String secret, String payload)
            throws InvalidKeyException, NoSuchAlgorithmException
    {
        String digest = "HmacSHA256";
        Mac mac = Mac.getInstance(digest);
        mac.init(new SecretKeySpec(secret.getBytes(), digest));
        return new String(Base64.getEncoder().encode(mac.doFinal(payload.getBytes())));
    }
    public static boolean HashIsValid(String secret, String payload, String verify)
            throws InvalidKeyException, NoSuchAlgorithmException, UnsupportedEncodingException
    {
        String computedHash = ComputeHash(secret, payload);
        return MessageDigest.isEqual(computedHash.getBytes(StandardCharsets.UTF_8),
                verify.getBytes(StandardCharsets.UTF_8));
    }
}

