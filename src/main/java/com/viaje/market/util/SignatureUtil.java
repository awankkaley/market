package com.viaje.market.util;

import com.viaje.market.config.CoinsbitConfiguration;
import com.viaje.market.config.HotbitConfiguration;
import com.viaje.market.dto.CoinsbitSignature;
import lombok.extern.slf4j.Slf4j;
import net.minidev.json.JSONObject;
import net.minidev.json.JSONStyle;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Timestamp;
import java.util.Base64;

import static javax.xml.crypto.dsig.SignatureMethod.HMAC_SHA512;

@Slf4j
public class SignatureUtil {

    public static String GenerateSignatureHotbit(String data, HotbitConfiguration hotbitConfiguration) {
        String input = data + "&secret_key=" + hotbitConfiguration.getSecret();
        String result = null;
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            md.update(input.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < bytes.length; i++) {
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            result = sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return result.toUpperCase();
    }

    public static CoinsbitSignature GenerateSignatureCoinsbit(String body, CoinsbitConfiguration coinsbitConfiguration) {

        try {
            String result = body.replace("\\", "");
            String payload = Base64.getEncoder().encodeToString(result.getBytes(StandardCharsets.UTF_8));
            Mac sha256_HMAC = Mac.getInstance("HmacSHA512");
            SecretKeySpec secret_key = new SecretKeySpec(coinsbitConfiguration.getSecret().getBytes(), "HmacSHA512");
            sha256_HMAC.init(secret_key);
            String hash = Base64.getEncoder().encodeToString(sha256_HMAC.doFinal(payload.getBytes()));
            return new CoinsbitSignature(hash, payload, result);

        } catch (Exception e) {
            e.printStackTrace();
            throw new IllegalArgumentException(e.getMessage());
        }
    }
}
