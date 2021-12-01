package com.viaje.market.dtos.coinsbit_market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viaje.market.dtos.response.MarketResponse;
import com.viaje.market.dtos.response.MarketResponseData;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data
class CoinsbitMarketDto {
    private CoinsbitMarketResultDto result;
    private boolean success;
    private String message;

    public MarketResponse toResponse() {
        MarketResponseData data = MarketResponseData.builder()
                .deal(result.getDeal())
                .high(result.getHigh())
                .last(result.getLast())
                .low(result.getLow())
                .open(result.getOpen())
                .volume(result.getVolume())
                .build();
        return MarketResponse.builder()
                .data(data)
                .exchangeCode(2)
                .exchange("Coinsbit")
                .build();
    }
}