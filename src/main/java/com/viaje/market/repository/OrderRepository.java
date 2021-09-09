package com.viaje.market.repository;

import com.viaje.market.entity.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity,Long> {

    @Query(value = "SELECT o FROM OrderEntity o")
    Page<OrderEntity> findAllOrdersWithPagination(Pageable pageable);
}
