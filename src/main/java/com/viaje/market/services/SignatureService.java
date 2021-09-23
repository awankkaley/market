package com.viaje.market.services;

public interface SignatureService {
    boolean isValidSignature(String payload, String verify);
}
