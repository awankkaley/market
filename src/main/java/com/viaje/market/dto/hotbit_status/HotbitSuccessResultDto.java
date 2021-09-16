package com.viaje.market.dto.hotbit_status;

import lombok.Data;

import java.util.List;

@Data
public class HotbitSuccessResultDto {
    private int offset;

    private int limit;

    private List<HotbitSuccessRecordDto> records;
}
