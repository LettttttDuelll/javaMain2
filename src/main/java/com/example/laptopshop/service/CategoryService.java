package com.example.laptopshop.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.laptopshop.entity.Category;
import com.example.laptopshop.repository.CategoryRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategoryService {

    private final CategoryRepository categoryRepo;

    public List<Category> getAll() {
        return categoryRepo.findAll();
    }

    public Category getById(Long id) {
        return categoryRepo.findById(id).orElse(null);
    }

    public void save(Category category) {
        categoryRepo.save(category);
    }

    public void delete(Long id) {
        Category c = getById(id);
        c.setActive(false);
        categoryRepo.save(c);
    }

    public void toggleStatus(Long id){
        Category c = getById(id);

        if(c != null){
            c.setActive(!c.isActive());
            categoryRepo.save(c);
        }
    }
}