package com.viaje.market.dto.coinsbit_balance;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

public @Data class CoinsbitbalanceResultDto {
	@JsonProperty("BSI")
	private CoinsbitBalanceResultBsiDto bSI;
	@JsonProperty("USDT")
	private CoinsbitBalanceResultUsdtDto uSDT;
}