package com.viaje.market.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viaje.market.dtos.hotbit_order.OrderResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class GlobalExchangeResponse {
    @JsonProperty("error")
    private HotbitErrorDto error;

    @JsonProperty("result")
    private OrderResponseDto result;

}
