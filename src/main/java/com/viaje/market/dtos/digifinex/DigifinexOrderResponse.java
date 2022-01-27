package com.viaje.market.dtos.digifinex;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DigifinexOrderResponse{
	private int code;
	private String order_id;
}