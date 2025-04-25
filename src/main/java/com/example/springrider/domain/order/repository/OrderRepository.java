package com.example.springrider.domain.order.repository;

import com.example.springrider.domain.order.entity.Order;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("""
        SELECT DISTINCT o FROM Order o
        JOIN FETCH o.orderItems oi
        JOIN FETCH oi.menu
        WHERE o.id = :orderId
        """)
    Optional<Order> findByIdWithOrderItemsAndMenu(@Param("orderId") Long orderId);

}
