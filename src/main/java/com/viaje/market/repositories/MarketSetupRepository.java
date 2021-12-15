package com.viaje.market.repositories;

import com.viaje.market.entities.MarketSetupEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MarketSetupRepository extends JpaRepository<MarketSetupEntity, Long> {
}
