package com.example.laptopshop.repository;

import com.example.laptopshop.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Integer> {
    List<Order> findByUser_Id(Integer userId);

    @Query("SELECT o FROM Order o JOIN FETCH o.user")
    List<Order> findAllWithUser(); // ✅ thêm dòng này
}

