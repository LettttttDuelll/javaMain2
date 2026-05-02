package com.example.laptopshop.repository;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.laptopshop.entity.Voucher;

@Repository
public interface VoucherRepository extends JpaRepository<Voucher, Integer>{
    Voucher findByCode(String code);

    boolean existsByCode(String code);
    
}
