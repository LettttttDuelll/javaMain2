package com.example.laptopshop.repository;

import com.example.laptopshop.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartItemRepository extends JpaRepository<CartItem, Integer> {
    List<CartItem> findByUserId(Long userId);
    //Optional<CartItem> findByUserIdAndProductId(Long userId, Long productId);
    void deleteByUserId(Long userId);
    Optional<CartItem> findByUserIdAndLaptopId(Long id, Long laptopId);
}

