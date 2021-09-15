package com.viaje.market.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CoinsbitSignature {
    private String signature;
    private String payload;
    private String jsonBody;


}
