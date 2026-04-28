package com.example.laptopshop.entity;

import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "vouchers")
public class Voucher {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;          // tên chương trình
    private String code;          // BACK2SCHOOL

    private String discountType; // FIXED / PERCENT
    private Double discountValue;

    private Double minOrderValue;

    private LocalDate startDate;
    private LocalDate endDate;

    private Boolean active = true;

    private String note;
}
