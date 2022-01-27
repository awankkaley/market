package com.viaje.market.entities;


import com.viaje.market.dtos.digifinex.DigifinexStatusDataItem;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "digifinex_data")
public class DigifinexEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int executedAmount;
    private String symbol;
    private double amount;
    private int cashAmount;
    private String kind;
    private double price;
    private int finishedDate;
    private int avgPrice;
    private int createdDate;
    private String type;
    private String orderId;
    private int status;

    public DigifinexStatusDataItem toDto() {
        return DigifinexStatusDataItem.builder()
                .id(id)
                .executed_amount(executedAmount)
                .symbol(symbol)
                .amount(amount)
                .cash_amount(cashAmount)
                .kind(kind)
                .price(price)
                .finished_date(finishedDate)
                .avg_price(avgPrice)
                .created_date(createdDate)
                .type(type)
                .order_id(orderId)
                .status(status).build();
    }

    @Override
    public String toString() {
        return "DigifinexEntity{" +
                "id=" + id +
                ", executedAmount=" + executedAmount +
                ", symbol='" + symbol + '\'' +
                ", amount=" + amount +
                ", cashAmount=" + cashAmount +
                ", kind='" + kind + '\'' +
                ", price=" + price +
                ", finishedDate=" + finishedDate +
                ", avgPrice=" + avgPrice +
                ", createdDate=" + createdDate +
                ", type='" + type + '\'' +
                ", orderId='" + orderId + '\'' +
                ", status=" + status +
                '}';
    }
}
