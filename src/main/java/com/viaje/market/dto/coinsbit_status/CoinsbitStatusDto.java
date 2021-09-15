package com.viaje.market.dto.coinsbit_status;

import lombok.Data;

public @Data class CoinsbitStatusDto{
	private CoinsbitStatusResultDto result;
	private boolean success;
	private String message;
}