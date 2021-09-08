package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitBookDto {
    private HotbitErrorDto error = null;

    private HotbitBookResultDto result;

    private long id;
}
