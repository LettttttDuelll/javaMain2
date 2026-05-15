package com.example.laptopshop.dto;

public class RevenueChartDTO {

    private String date;
    private Double revenue;

    public RevenueChartDTO(String date, Double revenue) {
        this.date = date;
        this.revenue = revenue;
    }

    public String getDate() {
        return date;
    }

    public Double getRevenue() {
        return revenue;
    }
}