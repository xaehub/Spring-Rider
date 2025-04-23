package com.example.springrider.domain.user.entity;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.user.enums.UserRole;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "user")
@NoArgsConstructor
@AllArgsConstructor
public class User extends BaseEntity {

    private String email;
    private String password;
    private String name;
    private String nickname;
    private String phone;
    private UserRole role;
    private Boolean isWithdraw;
    private Integer store_count;

}