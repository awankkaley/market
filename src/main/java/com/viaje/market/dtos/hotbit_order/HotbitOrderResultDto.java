package com.viaje.market.dtos.hotbit_order;

import com.viaje.market.entities.HotbitEntity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
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

    private String freeze;

    public HotbitEntity toEntity() {
        return HotbitEntity.builder()
                .orderId(id)
                .market(market)
                .source(source)
                .type(type)
                .side(side)
                .user(user)
                .ctime(ctime)
                .mtime(mtime)
                .price(price)
                .amount(amount)
                .taker_fee(taker_fee)
                .maker_fee(maker_fee)
                .leftData(left)
                .deal_stock(deal_stock)
                .deal_money(deal_money)
                .deal_fee(deal_fee)
                .status(status)
                .fee_stock(fee_stock)
                .alt_fee(alt_fee)
                .deal_fee_alt(deal_fee_alt)
                .freeze(freeze)
                .build();
    }

}

