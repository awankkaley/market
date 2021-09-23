package com.viaje.market.dtos.base_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BaseError {
    private Integer code;
    private String Message;
}
