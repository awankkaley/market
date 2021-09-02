package com.viaje.market.base_dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
@AllArgsConstructor
public class GlobalMultipleErrorDto{
    private Integer code;
    private String error;
    private List<String> message;
}
