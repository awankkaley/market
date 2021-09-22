package com.viaje.market.dto.hotbit_order;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viaje.market.dto.HotbitErrorDto;
import com.viaje.market.entity.HotbitEntity;
import lombok.Data;

@Data
public class HotbitOrderResponseDto {
    @JsonProperty("error")
    private HotbitErrorDto error;

    @JsonProperty("result")
    private HotbitOrderResultDto result;

    private long id;



}
