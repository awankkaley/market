package com.viaje.market.dtos.digifinex;

import lombok.Data;

@Data
public class DigifinexBalanceListItem {
	private int total;
	private String currency;
	private int free;
}