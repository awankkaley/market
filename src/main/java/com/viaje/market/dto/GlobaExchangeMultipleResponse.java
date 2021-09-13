package com.viaje.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
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

