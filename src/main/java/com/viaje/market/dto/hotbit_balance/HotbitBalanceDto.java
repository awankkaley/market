package com.viaje.market.dto.hotbit_balance;

import com.viaje.market.dto.HotbitErrorDto;
import com.viaje.market.dto.response.BalanceAssetsResponseDto;
import com.viaje.market.dto.response.BalanceResponseDto;
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


