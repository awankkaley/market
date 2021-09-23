package com.viaje.market.dtos.base_dto;

import com.viaje.market.dtos.HotbitErrorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseDto {
    protected HotbitErrorDto error;
}
