package com.example.springrider.domain.store.repository;

import com.example.springrider.domain.store.entity.Store;
import com.example.springrider.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StoreRepository extends JpaRepository<Store, Long> {

    long countByUser(User user); // 사장님이 등록한 가게 개수
}
