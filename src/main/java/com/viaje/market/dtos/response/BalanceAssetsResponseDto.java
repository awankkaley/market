package com.viaje.market.dtos.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class BalanceAssetsResponseDto {
    private String asset;
    private String available;
    private String freeze;
}
