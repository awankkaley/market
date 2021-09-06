package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitTodayDto {
    private String error;

    private HotbitTodayResultDto result;

    private int id;
}