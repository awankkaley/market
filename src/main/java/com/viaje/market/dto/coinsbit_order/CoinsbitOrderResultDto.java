package com.viaje.market.dto.coinsbit_order;

import lombok.Data;

public @Data class CoinsbitOrderResultDto {
	private String side;
	private String amount;
	private String takerFee;
	private long orderId;
	private String dealStock;
	private String dealFee;
	private String type;
	private String market;
	private String makerFee;
	private String left;
	private String price;
	private double timestamp;
	private String dealMoney;
}