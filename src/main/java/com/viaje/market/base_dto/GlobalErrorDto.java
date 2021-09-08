package com.viaje.market.base_dto;

import com.viaje.market.dto.HotbitErrorDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@AllArgsConstructor
public class GlobalErrorDto {
    BaseError error;
}
