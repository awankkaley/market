package com.viaje.market.dto;

import lombok.Data;

@Data
public class HotbitOrdersDto {
    private long id;

    private String market;

    private int type;

    private int side;

    private double ctime;

    private double mtime;

    private String price;

    private String amount;

    private String left;

    private String deal_stock;

    private String deal_money;

    private int status;
}

