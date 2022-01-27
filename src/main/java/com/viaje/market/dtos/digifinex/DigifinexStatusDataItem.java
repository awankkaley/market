package com.viaje.market.dtos.digifinex;

import com.viaje.market.entities.DigifinexEntity;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@ToString
public class DigifinexStatusDataItem {
    private long id;
    private int executed_amount;
    private String symbol;
    private double amount;
    private int cash_amount;
    private String kind;
    private double price;
    private int finished_date;
    private int avg_price;
    private int created_date;
    private String type;
    private String order_id;
    private int status;

    public DigifinexEntity toEntity() {
        return DigifinexEntity.builder()
                .executedAmount(executed_amount)
                .symbol(symbol)
                .amount(amount)
                .cashAmount(cash_amount)
                .kind(kind)
                .price(price)
                .finishedDate(finished_date)
                .avgPrice(avg_price)
                .createdDate(created_date)
                .type(type)
                .orderId(order_id)
                .status(status).build();
    }
}