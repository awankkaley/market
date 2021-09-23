package com.viaje.market.dtos.hotbit_status;

import com.viaje.market.dtos.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitSuccessResponseDto {
    private HotbitErrorDto error = null;

    private HotbitSuccessResultDto result;

    private long id;

}



