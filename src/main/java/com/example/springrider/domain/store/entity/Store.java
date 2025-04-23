package com.example.springrider.domain.store.entity;

import com.example.springrider.domain.common.entity.BaseEntity;
import com.example.springrider.domain.menu.entity.Menu;
import com.example.springrider.domain.store.enums.StoreStatus;
import com.example.springrider.domain.user.entity.User;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "store")
@AllArgsConstructor
@NoArgsConstructor
public class Store extends BaseEntity {

    private String name;
    private String address;
    private String category;
    private LocalTime openTime;
    private LocalTime closeTime;
    private Integer minOrderPrice;

    @Enumerated(EnumType.STRING)
    private StoreStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "store", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Menu> menus = new ArrayList<>();

    public Store(String name, String address, String category, LocalTime openTime,
        LocalTime closeTime, Integer minOrderPrice, StoreStatus status, User user) {
        this.name = name;
        this.address = address;
        this.category = category;
        this.openTime = openTime;
        this.closeTime = closeTime;
        this.minOrderPrice = minOrderPrice;
        this.status = status;
        this.user = user;
        this.menus = new ArrayList<>();
    }


}
