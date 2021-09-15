package com.viaje.market.dto.coinsbit_order;

import lombok.Data;

public @Data class CoinsbitOrderDto{
	private CoinsbitOrderResultDto result;
	private boolean success;
	private String message;
}