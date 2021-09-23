package com.viaje.market.repositories;

import com.viaje.market.entities.OrderEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, Long> {

    @Query(value = "SELECT o FROM OrderEntity o")
    Page<OrderEntity> findAllOrdersWithPagination(Pageable pageable);

    @Query(value = "SELECT o FROM OrderEntity o WHERE o.status = :status")
    Page<OrderEntity> findAllOrdersWithPaginationAnStatus(Pageable pageable, @Param("status") Integer status);

    Optional<OrderEntity> findByIdAndStatus(Long id, Integer status);

    List<OrderEntity> findByStatusAndIsValid(Integer status, boolean valid);
}
