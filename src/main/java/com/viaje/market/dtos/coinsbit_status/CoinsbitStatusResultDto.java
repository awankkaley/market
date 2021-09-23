package com.viaje.market.dtos.coinsbit_status;

import java.util.List;
import lombok.Data;

public @Data class CoinsbitStatusResultDto {
	private int offset;
	private List<CoinsbitStatusRecordsItem> records;
	private int limit;
}