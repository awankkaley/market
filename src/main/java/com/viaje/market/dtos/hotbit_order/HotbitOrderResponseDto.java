package com.viaje.market.dtos.hotbit_order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viaje.market.dtos.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitOrderResponseDto {
    @JsonProperty("error")
    private HotbitErrorDto error;

    @JsonProperty("result")
    private HotbitOrderResultDto result;

    private long id;



}
