package com.viaje.market.dtos.hotbit_order;

import com.viaje.market.dtos.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitBookDto {
    private HotbitErrorDto error = null;

    private HotbitBookResultDto result;

    private long id;
}
