package com.viaje.market.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MarketResponse {
    private String exchange;

    private Integer exchangeCode;

    private MarketResponseData data;

}
