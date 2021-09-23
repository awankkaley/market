package com.viaje.market.dtos.hotbit_order;

import lombok.Data;

import java.util.List;

@Data
public class HotbitBookResultDto {
    private int limit;

    private int offset;

    private int total;

    private List<HotbitOrdersDto> orders;

}
