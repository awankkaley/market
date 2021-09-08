package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitBalanceDto {
    private HotbitErrorDto error = null;
    private HotbitBalanceResultDto result;
    private int id;
}
