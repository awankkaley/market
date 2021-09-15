package com.viaje.market.dto.coinsbit_market;

import lombok.Data;

public @Data class CoinsbitMarketDto{
	private CoinsbitMarketResultDto result;
	private boolean success;
	private String message;
}