package com.viaje.market.dto.hotbit_market;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HotbitPeriodResultDto {
    @JsonProperty("IsChange")
    private boolean IsChange;

    private int period;

    private String open;

    private String last;

    private String high;

    private String low;

    private String volume;

    private String deal;

    private String close;

    private String base_volume;

    private String quote_volume;
}
