package com.viaje.market.dto.coinsbit_status;

import lombok.Data;

public @Data class CoinsbitStatusRecordsItem {
	private String amount;
	private String deal;
	private int role;
	private String price;
	private String fee;
	private double time;
	private long id;
	private long dealOrderId;
}