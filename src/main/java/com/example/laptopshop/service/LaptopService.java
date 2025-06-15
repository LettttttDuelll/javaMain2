package com.example.laptopshop.service;


import java.util.List;
import java.util.Optional;

import com.example.laptopshop.dto.LaptopDTO;
import com.example.laptopshop.entity.Laptop;
import org.springframework.web.multipart.MultipartFile;

public interface LaptopService {
    public boolean addLaptop(LaptopDTO laptopDTO, MultipartFile[] imageFiles);
    public boolean editLaptop(Long id , LaptopDTO laptopDTO , MultipartFile[] imageFiles);
    public boolean deleteLaptop(Long id);
    public List<Laptop> getAllLaptops();
    public Optional<Laptop> getLaptopById(Long id);


}
