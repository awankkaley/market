package com.viaje.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class GlobalExchangeResponse {
    @JsonProperty("error")
    private HotbitErrorDto error;

    @JsonProperty("result")
    private OrderResponseDto result;

}
