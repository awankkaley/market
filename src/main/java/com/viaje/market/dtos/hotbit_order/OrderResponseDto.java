package com.viaje.market.dtos.hotbit_order;

import lombok.*;

@Data
@Builder
@Getter
@Setter
@AllArgsConstructor
public class OrderResponseDto {

    private Long id;

    private String exchangeOrderId;

    private String exchangeCode;

    private String side;

    private Double amount;

    private Double currentPrice;

    private Double price;

    private Integer status;

    private String statusTitle;


    private String info;

    private boolean isValid;
}
