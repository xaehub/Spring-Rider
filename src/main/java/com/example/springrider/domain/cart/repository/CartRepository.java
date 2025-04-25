package com.example.springrider.domain.cart.repository;

import com.example.springrider.domain.cart.entity.CartItem;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    @Query("SELECT ci FROM CartItem ci " +
        "JOIN FETCH ci.menu m " +
        "JOIN FETCH m.store " +
        "WHERE ci.user.id = :userId AND ci.modifiedAt > :limit")
    List<CartItem> findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
        @Param("userId") Long userId,
        @Param("limit") LocalDateTime limit);
}
