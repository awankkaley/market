package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitBookDto {
    private String error;

    private HotbitBookResultDto result;

    private long id;
}
