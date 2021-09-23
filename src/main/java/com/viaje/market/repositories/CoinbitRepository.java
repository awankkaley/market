package com.viaje.market.repositories;

import com.viaje.market.entities.CoinsbitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinbitRepository extends JpaRepository<CoinsbitEntity, Long> {
    Optional<CoinsbitEntity> findByOrderId(Long orderId);

}
