package com.example.springrider.domain.review.repository;

import com.example.springrider.domain.review.entity.Review;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    boolean existsByOrderId(Long orderId);

    @Query("""
        SELECT DISTINCT r FROM Review r
        JOIN FETCH r.order o
        JOIN FETCH o.store s
        WHERE s.id = :storeId
        """)
    List<Review> findAllByStoreId(
        @Param("storeId") Long storeId);
}
