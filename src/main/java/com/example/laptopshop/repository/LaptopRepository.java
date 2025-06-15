package com.example.laptopshop.repository;

import java.time.LocalDateTime;
import java.util.List;


import com.example.laptopshop.entity.Laptop;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;



@Repository
public interface LaptopRepository extends JpaRepository<Laptop,Long> {
    List<Laptop> findAllByDeletedTrueAndRestoredFalseAndDeletedAtBefore(LocalDateTime time);
    List<Laptop> findAllByDeletedTrue();
    List<Laptop> findByDeletedFalse();
    @Query("SELECT l.model, SUM(l.stock) FROM Laptop l WHERE l.deleted = false GROUP BY l.model")
    List<Object[]> countByModel();
    @Query("SELECT l.discount, SUM(l.stock) FROM Laptop l WHERE l.deleted = false GROUP BY l.discount ORDER BY l.discount ASC")
    List<Object[]> stockByDiscount();


}