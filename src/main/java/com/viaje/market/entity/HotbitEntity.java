package com.viaje.market.entity;


import com.viaje.market.base_dto.BaseTimeEntity;
import com.viaje.market.dto.hotbit_order.HotbitOrderResultDto;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "hotbit_data")
public class HotbitEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long orderId;

    private String market;

    private String source;

    private Integer type;

    private Integer side;

    private Long user;

    private Double ctime;

    private Double mtime;

    private String price;

    private String amount;

    private String taker_fee;

    private String maker_fee;

    private String leftData;

    private String deal_stock;

    private String deal_money;

    private String deal_fee;

    private Integer status;

    private String fee_stock;

    private String alt_fee;

    private String deal_fee_alt;

    private String freeze;

    public HotbitOrderResultDto toDto() {
        return HotbitOrderResultDto.builder()
                .id(orderId)
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
                .left(leftData)
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

    @Override
    public String toString() {
        return "HotbitEntity{" +
                "id=" + id +
                ", orderId=" + orderId +
                ", market='" + market + '\'' +
                ", source='" + source + '\'' +
                ", type=" + type +
                ", side=" + side +
                ", user=" + user +
                ", ctime=" + ctime +
                ", mtime=" + mtime +
                ", price='" + price + '\'' +
                ", amount='" + amount + '\'' +
                ", taker_fee='" + taker_fee + '\'' +
                ", maker_fee='" + maker_fee + '\'' +
                ", leftData='" + leftData + '\'' +
                ", deal_stock='" + deal_stock + '\'' +
                ", deal_money='" + deal_money + '\'' +
                ", deal_fee='" + deal_fee + '\'' +
                ", status=" + status +
                ", fee_stock='" + fee_stock + '\'' +
                ", alt_fee='" + alt_fee + '\'' +
                ", deal_fee_alt='" + deal_fee_alt + '\'' +
                ", freeze='" + freeze + '\'' +
                '}';
    }
}
