package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitTodayDto {
    private HotbitErrorDto error = null;

    private HotbitTodayResultDto result;

    private int id;
}