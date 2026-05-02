package com.example.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.laptopshop.entity.Voucher;
import com.example.laptopshop.service.VoucherService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin/vouchers")
@RequiredArgsConstructor
public class VoucherController {
    private final VoucherService service;

    // danh sách
    @GetMapping
    public String list(Model model) {
        model.addAttribute("vouchers", service.getAll());
        return "dashboard/voucher";
    }

    // lưu thêm / sửa
    @PostMapping("/save")
    public String save(@ModelAttribute Voucher voucher) {
        service.save(voucher);
        return "redirect:/admin/vouchers?success";
    }

    // bật tắt trạng thái
    @GetMapping("/toggle/{id}")
    public String toggle(@PathVariable Integer id) {
        service.toggle(id);
        return "redirect:/admin/vouchers?updated";
    }
}
