package com.viaje.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HotbitOrderResponseDto {
    @JsonProperty("error")
    private HotbitErrorDto error;

    @JsonProperty("result")
    private HotbitOrderResultDto result;

    private long id;
}
