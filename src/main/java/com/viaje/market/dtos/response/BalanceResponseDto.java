package com.viaje.market.dtos.response;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@AllArgsConstructor
@Data
public class BalanceResponseDto {
    private String exchange;
    private Integer exchangeCode;
    private List<BalanceAssetsResponseDto> assets;
}
