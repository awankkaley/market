package com.viaje.market.repository;

import com.viaje.market.entity.HotbitEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HotbitRepository extends JpaRepository<HotbitEntity, Long> {
    Optional<HotbitEntity> findByOrderId(Long orderId);

}
