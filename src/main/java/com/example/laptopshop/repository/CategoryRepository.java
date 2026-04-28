package com.example.laptopshop.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.laptopshop.entity.Category;
import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Long>{
    List<Category> findByActiveTrue();
}
