package com.viaje.market.service;

public interface SignatureService {
    boolean isValidSignature(String payload, String verify);
}
