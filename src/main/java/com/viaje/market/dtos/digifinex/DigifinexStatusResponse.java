package com.viaje.market.dtos.digifinex;

import java.util.List;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class DigifinexStatusResponse{
	private int code;
	private List<DigifinexStatusDataItem> data;
}