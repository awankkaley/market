package com.viaje.market.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viaje.market.dtos.hotbit_order.OrderResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class GlobaExchangeMultipleResponse {
    @JsonProperty("error")
    private HotbitErrorDto error;

    @JsonProperty("result")
    private List<OrderResponseDto> result;

}

