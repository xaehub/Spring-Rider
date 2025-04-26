package com.example.springrider.domain.cart.repository;

import com.example.springrider.domain.cart.entity.CartItem;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface CartRepository extends JpaRepository<CartItem, Long> {

    // 기준 시간 이후에 수정되었으며 유저 아이디가 같은 장바구니를 조회한다.
    // menu 테이블과 menu 테이블 안의 store 테이블이 fetch Join이 되었다.
    @Query("SELECT ci FROM CartItem ci " +
        "JOIN FETCH ci.menu m " +
        "JOIN FETCH m.store " +
        "WHERE ci.user.id = :userId AND ci.modifiedAt > :limit")
    List<CartItem> findAllByUserIdAndModifiedAtAfterWithMenuAndStore(
        @Param("userId") Long userId,
        @Param("limit") LocalDateTime limit);

    // 기준 시간 이후에 수정되었으며 유저 아이디가 같은 장바구니를 조회한다.
    // menu 테이블과 fetch Join이 되었다.
    @Query("SELECT ci FROM CartItem ci " +
        "JOIN FETCH ci.menu " +
        "WHERE ci.user.id = :userId AND ci.modifiedAt > :limit")
    List<CartItem> findAllbyUserIdAndModifiedAtAfterWithMenu(
        @Param("userId") Long userId,
        @Param("limit") LocalDateTime limit);

    @Query("SELECT ci FROM CartItem ci " +
        "JOIN FETCH ci.user " +
        "WHERE ci.id = :id")
    Optional<CartItem> findByIdWithUser(
        @Param("id") Long cartItemId);
}
