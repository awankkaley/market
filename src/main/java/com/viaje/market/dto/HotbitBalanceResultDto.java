package com.viaje.market.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HotbitBalanceResultDto {
    @JsonProperty("BSI")
    private HotbitBsiDto Bsi;

    @JsonProperty("USDT")
    private HotbitUsdtDto Usdt;

}
