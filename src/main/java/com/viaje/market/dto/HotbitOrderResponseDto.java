package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitOrderResponseDto {
    private String error;

    private HotbitOrderResultDto result;

    private int id;
}
