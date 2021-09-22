package com.viaje.market.repository;

import com.viaje.market.entity.CoinsbitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CoinbitRepository extends JpaRepository<CoinsbitEntity, Long> {
    Optional<CoinsbitEntity> findByOrderId(Long orderId);

}
