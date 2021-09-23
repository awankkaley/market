package com.viaje.market.dtos.coinsbit_status;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CoinsbitStatusDto{
	private CoinsbitStatusResultDto result;
	private boolean success;
	private String message;
}