package com.viaje.market.dto.hotbit_order;

import lombok.Data;

@Data
public class HotbitOrderResultDto {
    private long id;

    private String market;

    private String source;

    private int type;

    private int side;

    private long user;

    private double ctime;

    private double mtime;

    private String price;

    private String amount;

    private String taker_fee;

    private String maker_fee;

    private String left;

    private String deal_stock;

    private String deal_money;

    private String deal_fee;

    private int status;

    private String fee_stock;

    private String alt_fee;

    private String deal_fee_alt;

    private String freeze = null;

}

