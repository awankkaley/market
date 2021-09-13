package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitSuccessResponseDto {
    private HotbitErrorDto error = null;

    private HotbitSuccessResultDto result;

    private long id;

}



