package com.viaje.market.dto.hotbit_status;

import com.viaje.market.dto.HotbitErrorDto;
import lombok.Data;

@Data
public class HotbitSuccessResponseDto {
    private HotbitErrorDto error = null;

    private HotbitSuccessResultDto result;

    private long id;

}



