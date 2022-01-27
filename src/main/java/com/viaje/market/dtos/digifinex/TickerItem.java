package com.viaje.market.dtos.digifinex;

import lombok.Data;

@Data
public class TickerItem{
	private String symbol;
	private double vol;
	private double high;
	private double last;
	private double low;
	private double change;
	private double sell;
	private double buy;
	private double base_vol;
}