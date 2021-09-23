package com.viaje.market.dtos.hotbit_market;

import com.viaje.market.dtos.HotbitErrorDto;
import com.viaje.market.dtos.response.MarketResponse;
import com.viaje.market.dtos.response.MarketResponseData;
import lombok.Data;

@Data
public class HotbitTodayDto {
    private HotbitErrorDto error = null;

    private HotbitTodayResultDto result;

    private int id;

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
                .exchangeCode(1)
                .exhange("Hotbit")
                .build();

    }
}