package com.example.springrider.domain.user.repository;

import com.example.springrider.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email); // 중복여부
    Optional<User> findByEmail(String email); // 이메일로 유저조회
}
