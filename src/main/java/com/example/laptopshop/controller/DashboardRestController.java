package com.example.laptopshop.controller;

import com.example.laptopshop.dto.RevenueChartDTO;
import com.example.laptopshop.dto.RevenueDTO;
import com.example.laptopshop.dto.TopProductDTO;
import com.example.laptopshop.entity.Laptop;
import com.example.laptopshop.repository.LaptopRepository;
import com.example.laptopshop.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
public class DashboardRestController {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private LaptopRepository laptopRepository;

    @GetMapping("/api/dashboard/revenue")
    public RevenueDTO getRevenue(
            @RequestParam("start")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate start,

            @RequestParam("end")
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate end
    ) {

        LocalDateTime startDate = start.atStartOfDay();
        LocalDateTime endDate = end.atTime(LocalTime.MAX);

        return orderRepository.getRevenueStatistics(startDate, endDate);
    }

    @GetMapping("/api/dashboard/top-products")
    public List<TopProductDTO> getTopProducts() {
        return orderRepository.getTopProducts();
    }

    @GetMapping("/api/dashboard/revenue-chart")
public List<RevenueChartDTO> getRevenueChart(

        @RequestParam("start")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate start,

        @RequestParam("end")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate end
) {

    LocalDateTime startDate = start.atStartOfDay();
    LocalDateTime endDate = end.atTime(LocalTime.MAX);

    List<Object[]> results =
            orderRepository.getRevenueByDate(startDate, endDate);

    return results.stream()
            .map(r -> new RevenueChartDTO(
                    r[0].toString(),
                    ((Number) r[1]).doubleValue()
            ))
            .toList();
}

    @GetMapping("/api/dashboard/low-stock")
    public List<Laptop> getLowStock() {
        return laptopRepository.findTop5ByStockLessThanAndDeletedFalse(5);
    }
}