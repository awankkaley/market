package com.viaje.market.dtos.coinsbit_balance;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@JsonIgnoreProperties(ignoreUnknown = true)
public @Data class CoinsbitbalanceResultDto {
	@JsonProperty("BSI")
	private CoinsbitBalanceResultBsiDto bSI;
	@JsonProperty("USDT")
	private CoinsbitBalanceResultUsdtDto uSDT;
}