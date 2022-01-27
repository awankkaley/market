package com.viaje.market.dtos.digifinex;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viaje.market.dtos.response.MarketResponse;
import com.viaje.market.dtos.response.MarketResponseData;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigifinexMarketResponse {
    private int date;
    private List<TickerItem> ticker;
    private int code;

    public MarketResponse toResponse() {
        MarketResponseData data = MarketResponseData.builder()
                .deal(String.valueOf(ticker.get(0).getSell()))
                .high(String.valueOf(ticker.get(0).getHigh()))
                .last(String.valueOf(ticker.get(0).getLast()))
                .low(String.valueOf(ticker.get(0).getLow()))
                .open(String.valueOf(ticker.get(0).getBuy()))
                .volume(String.valueOf(ticker.get(0).getVol()))
                .build();
        return MarketResponse.builder()
                .data(data)
                .exchangeCode(3)
                .exchange("Digifinex")
                .build();
    }

}