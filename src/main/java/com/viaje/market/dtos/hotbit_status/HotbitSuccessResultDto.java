package com.viaje.market.dtos.hotbit_status;

import lombok.Data;

import java.util.List;

@Data
public class HotbitSuccessResultDto {
    private int offset;

    private int limit;

    private List<HotbitSuccessRecordDto> records;
}
