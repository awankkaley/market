package com.viaje.market.dto;

import lombok.Data;

import java.util.List;

@Data
public class HotbitBookResultDto {
    private int limit;

    private int offset;

    private int total;

    private List<HotbitOrdersDto> orders;

}
