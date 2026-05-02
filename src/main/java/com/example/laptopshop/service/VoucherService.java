package com.example.laptopshop.service;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.example.laptopshop.entity.Voucher;
import com.example.laptopshop.repository.VoucherRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class VoucherService {
     private final VoucherRepository voucherRepo;

    public List<Voucher> getAll() {
        return voucherRepo.findAll();
    }

    public Voucher getById(Integer id) {
        return voucherRepo.findById(id).orElse(null);
    }

    public Voucher getByCode(String code) {
        //return voucherRepo.findByCode(code).orElse(null);
        return voucherRepo.findByCode(code);
    }

    public void save(Voucher voucher) {

        // code viết hoa cho đẹp
        voucher.setCode(voucher.getCode().trim().toUpperCase());

        voucherRepo.save(voucher);
    }

    public void toggle(Integer id) {
        Voucher voucher = getById(id);

        if (voucher != null) {
            voucher.setActive(!voucher.getActive());
            voucherRepo.save(voucher);
        }
    }

    public boolean existsCode(String code) {
        return voucherRepo.existsByCode(code.toUpperCase());
    }

    // kiểm tra voucher dùng khi checkout
    public String validateVoucher(String code, Double totalMoney) {

        Voucher voucher = getByCode(code.toUpperCase());

        if (voucher == null) {
            return "Mã giảm giá không tồn tại";
        }

        if (!voucher.getActive()) {
            return "Mã giảm giá đang tắt";
        }

        LocalDate today = LocalDate.now();

        if (today.isBefore(voucher.getStartDate())) {
            return "Mã chưa đến ngày sử dụng";
        }

        if (today.isAfter(voucher.getEndDate())) {
            return "Mã đã hết hạn";
        }

        if (totalMoney < voucher.getMinOrderValue()) {
            return "Đơn hàng chưa đủ giá trị tối thiểu";
        }

        return "OK";
    }

    // tính số tiền giảm
    public Double calculateDiscount(Voucher voucher, Double totalMoney) {

        if (voucher.getDiscountType().equalsIgnoreCase("FIXED")) {
            return voucher.getDiscountValue();
        }

        if (voucher.getDiscountType().equalsIgnoreCase("PERCENT")) {
            return totalMoney * voucher.getDiscountValue() / 100;
        }

        return 0.0;
    }

    public Map<String, Object> applyVoucher(String code, Double total) {

    Map<String, Object> rs = new HashMap<>();

    Voucher v = voucherRepo.findByCode(code);

    if (v == null) {
        rs.put("valid", false);
        rs.put("message", "Mã không tồn tại");
        return rs;
    }

    if (!v.getActive()) {
        rs.put("valid", false);
        rs.put("message", "Mã đã bị khóa");
        return rs;
    }

    LocalDate now = LocalDate.now();

    if (now.isBefore(v.getStartDate()) || now.isAfter(v.getEndDate())) {
        rs.put("valid", false);
        rs.put("message", "Mã đã hết hạn");
        return rs;
    }

    if (total < v.getMinOrderValue()) {
        rs.put("valid", false);
        rs.put("message", "Chưa đủ giá trị đơn tối thiểu");
        return rs;
    }

    double discount = 0;

    if (v.getDiscountType().equals("FIXED")) {
        discount = v.getDiscountValue();
    } else {
        discount = total * v.getDiscountValue() / 100;
    }

    double finalTotal = total - discount;

    rs.put("valid", true);
    rs.put("discount", discount);
    rs.put("finalTotal", finalTotal);

    return rs;
    }
}
