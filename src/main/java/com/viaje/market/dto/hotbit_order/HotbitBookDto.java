package com.viaje.market.dto.hotbit_order;

import com.viaje.market.dto.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitBookDto {
    private HotbitErrorDto error = null;

    private HotbitBookResultDto result;

    private long id;
}
