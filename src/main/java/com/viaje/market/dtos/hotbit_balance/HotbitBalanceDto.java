package com.viaje.market.dtos.hotbit_balance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.viaje.market.dtos.HotbitErrorDto;
import com.viaje.market.dtos.response.BalanceAssetsResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.io.Serializable;
import java.util.Arrays;

@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotbitBalanceDto implements Serializable {
    @JsonProperty("error")
    private HotbitErrorDto error;

    @JsonProperty("result")
    private HotbitBalanceResultDto result;

    @JsonProperty("id")
    private int id;

    public BalanceResponseDto toResponse() {
        return BalanceResponseDto.builder()
                .exchange("Hotbit")
                .exchangeCode(1)
                .assets(Arrays.asList(
                        new BalanceAssetsResponseDto("BSI", result.getBsi().getAvailable(), result.getBsi().getFreeze()),
                        new BalanceAssetsResponseDto("USDT", result.getUsdt().getAvailable(), result.getUsdt().getFreeze())))
                .build();
    }
}


