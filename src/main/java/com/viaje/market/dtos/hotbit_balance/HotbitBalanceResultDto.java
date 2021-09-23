package com.viaje.market.dtos.hotbit_balance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotbitBalanceResultDto {
    @JsonProperty("BSI")
    private HotbitBsiDto Bsi;

    @JsonProperty("USDT")
    private HotbitUsdtDto Usdt;

}
