package com.viaje.market.dto.coinsbit_order;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data
class CoinsbitOrderDto {
    private CoinsbitOrderResultDto result;
    private boolean success;
    private String message;
}