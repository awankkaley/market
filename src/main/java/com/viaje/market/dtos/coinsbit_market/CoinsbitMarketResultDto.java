package com.viaje.market.dtos.coinsbit_market;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CoinsbitMarketResultDto {
	private String volume;
	private String deal;
	private String high;
	private String last;
	private String low;
	private String open;
}