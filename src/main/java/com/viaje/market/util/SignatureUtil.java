package com.viaje.market.util;

import com.viaje.market.config.HotbitConfiguration;
import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class SignatureUtil {

    public static String GenerateSignature(String data, HotbitConfiguration hotbitConfiguration) {
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
}
