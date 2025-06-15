package com.example.laptopshop.controller;

import com.example.laptopshop.service.LaptopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/stats")
public class StatsController {
    @Autowired
    private LaptopServiceImpl laptopService;
    @GetMapping("/model")
    @ResponseBody
    public List<Map<String, Object>> getModelStats() {
        return laptopService.getLaptopStockStats();
    }
    @GetMapping("/stock-discount")
    @ResponseBody
    public List<Map<String, Object>> getStockByDiscount() {
        return laptopService.getStockByDiscount();
    }

}
