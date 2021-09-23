package com.viaje.market.dtos.hotbit_balance;

import com.viaje.market.dtos.HotbitErrorDto;
import com.viaje.market.dtos.response.BalanceAssetsResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import lombok.Data;

import java.util.Arrays;

@Data
public class HotbitBalanceDto {
    private HotbitErrorDto error;
    private HotbitBalanceResultDto result;
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


