package com.viaje.market.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MarketResponseData {
    private String open;

    private String last;

    private String high;

    private String low;

    private String volume;

    private String deal;
}
