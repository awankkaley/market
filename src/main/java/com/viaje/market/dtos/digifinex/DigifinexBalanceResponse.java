package com.viaje.market.dtos.digifinex;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.viaje.market.dtos.response.BalanceAssetsResponseDto;
import com.viaje.market.dtos.response.BalanceResponseDto;
import lombok.Data;

@Data
public class DigifinexBalanceResponse {
    private int code;
    private List<DigifinexBalanceListItem> list;

    public BalanceResponseDto toResponse() {
        List<BalanceAssetsResponseDto> asset = new ArrayList<BalanceAssetsResponseDto>();
        for (DigifinexBalanceListItem item : list) {
            asset.add(new BalanceAssetsResponseDto(item.getCurrency(), String.valueOf(item.getTotal()), String.valueOf(item.getTotal() - item.getFree())));
        }

        return BalanceResponseDto.builder()
                .exchange("Digifinex")
                .exchangeCode(3)
                .assets(asset)
                .build();
    }

}