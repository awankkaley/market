package com.viaje.market.dtos;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HotbitErrorDto {
    @JsonProperty("code")
    private Integer code;
    @JsonProperty("message")
    private String message;

}
