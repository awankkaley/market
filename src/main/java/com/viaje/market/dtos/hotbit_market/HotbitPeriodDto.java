package com.viaje.market.dtos.hotbit_market;

import com.viaje.market.dtos.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitPeriodDto {
    private HotbitErrorDto error = null;

    private HotbitPeriodResultDto result;

    private int id;
}
