package com.viaje.market.dtos.coinsbit_balance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viaje.market.dtos.response.BalanceAssetsResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import lombok.Data;

import java.util.Arrays;


@JsonIgnoreProperties(ignoreUnknown = true)
public @Data
class CoinsbitBalanceDto {
    private CoinsbitbalanceResultDto result;
    private boolean success;
    private String message;

    public BalanceResponseDto toResponse() {
        return BalanceResponseDto.builder()
                .exchange("Coinsbit")
                .exchangeCode(2)
                .assets(Arrays.asList(
                        new BalanceAssetsResponseDto("BSI", result.getBSI().getAvailable(), result.getBSI().getFreeze()),
                        new BalanceAssetsResponseDto("USDT", result.getUSDT().getAvailable(), result.getUSDT().getFreeze())))
                .build();
    }
}