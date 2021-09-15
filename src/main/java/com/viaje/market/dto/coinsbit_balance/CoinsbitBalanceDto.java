package com.viaje.market.dto.coinsbit_balance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;


@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CoinsbitBalanceDto{
	private CoinsbitbalanceResultDto result;
	private boolean success;
	private String message;
}