package com.viaje.market.dto.hotbit_market;

import com.viaje.market.dto.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitTodayDto {
    private HotbitErrorDto error = null;

    private HotbitTodayResultDto result;

    private int id;
}