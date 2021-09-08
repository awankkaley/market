package com.viaje.market.dto;


import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class HotbitErrorDto {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;

}
