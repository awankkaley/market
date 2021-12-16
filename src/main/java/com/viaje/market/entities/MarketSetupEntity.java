package com.viaje.market.entities;

import com.viaje.market.dtos.base_dto.BaseTimeEntity;
import lombok.*;

import javax.persistence.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "market_setup_entity")
public class MarketSetupEntity extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer buyPercent;

    private Integer profitPercent;

}
