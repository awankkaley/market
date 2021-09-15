package com.viaje.market.dto.coinsbit_balance;

import lombok.Data;


public @Data class CoinsbitBalanceDto{
	private CoinsbitbalanceResultDto result;
	private boolean success;
	private String message;
}