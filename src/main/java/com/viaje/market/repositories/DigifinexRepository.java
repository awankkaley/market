package com.viaje.market.repositories;

import com.viaje.market.entities.DigifinexEntity;
import com.viaje.market.entities.HotbitEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface DigifinexRepository extends JpaRepository<DigifinexEntity, Long> {
    Optional<DigifinexEntity> findByOrderId(String orderId);

}
