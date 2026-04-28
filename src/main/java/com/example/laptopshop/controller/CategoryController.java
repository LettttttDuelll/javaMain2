package com.example.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.laptopshop.entity.Category;
import com.example.laptopshop.service.CategoryService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService service;

    @GetMapping
    public String list(Model model){
        model.addAttribute("categories", service.getAll());
        return "dashboard/category";
    }

    @GetMapping("/create")
    public String createForm(Model model){
        model.addAttribute("category", new Category());
        return "dashboard/category/form";
    }

    @PostMapping("/save")
    public String save(@ModelAttribute Category category){
        service.save(category);
        return "redirect:/admin/categories";
    }

    @GetMapping("/edit/{id}")
    public String edit(@PathVariable Long id, Model model){
        model.addAttribute("category", service.getById(id));
        return "dashboard/category/form";
    }

    @GetMapping("/delete/{id}")
    public String delete(@PathVariable Long id){
        service.delete(id);
        return "redirect:/admin/categories";
    }

    @GetMapping("/toggle/{id}")
    public String toggle(@PathVariable Long id){
        service.toggleStatus(id);
        return "redirect:/admin/categories";
}
}
