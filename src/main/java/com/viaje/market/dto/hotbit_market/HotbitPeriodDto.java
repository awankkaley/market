package com.viaje.market.dto.hotbit_market;

import com.viaje.market.dto.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitPeriodDto {
    private HotbitErrorDto error = null;

    private HotbitPeriodResultDto result;

    private int id;
}
