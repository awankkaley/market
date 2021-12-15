package com.viaje.market.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class MarketSetupRequestDto {

    @NotNull(message = "Please provide a buyPercent")
    private Integer buyPercent;

    @NotNull(message = "Please provide a profitPercent")
    private Integer profitPercent;
}
