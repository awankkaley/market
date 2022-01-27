package com.viaje.market.dtos.digifinex;

import java.util.List;

import lombok.Data;

@Data
public class DigifinexCancelResponse {
    private int date;
    private int code;
    private List<String> success;
    private List<String> error;
}