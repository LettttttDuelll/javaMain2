package com.example.laptopshop.service;

import com.example.laptopshop.dto.LaptopDTO;
import com.example.laptopshop.entity.Laptop;
import com.example.laptopshop.repository.LaptopRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LaptopServiceImpl implements LaptopService{

    private final LaptopRepository laptopRepository;

    public LaptopServiceImpl(LaptopRepository laptopRepository) {
        this.laptopRepository = laptopRepository;
    }

    @Override
    @Transactional
    public boolean addLaptop(LaptopDTO laptopDTO, MultipartFile[] imageFiles) {
        if (laptopDTO == null) {
            return false;
        }

        try {
            Laptop laptop = new Laptop();

            laptop.setName(laptopDTO.getName());
            laptop.setModel(laptopDTO.getModel());
            laptop.setCpu_name(laptopDTO.getCpuName());
            laptop.setCores(laptopDTO.getCores());
            laptop.setThreads(laptopDTO.getThreads());
            laptop.setBase_speed(laptopDTO.getBaseSpeed());
            laptop.setMax_speed(laptopDTO.getMaxSpeed());
            laptop.setOriginal_price(laptopDTO.getOriginalPrice());
            laptop.setCurrent_price(laptopDTO.getCurrentPrice());
            laptop.setDiscount(laptopDTO.getDiscount());
            laptop.setDescription(laptopDTO.getDescription());
            laptop.setSize(laptopDTO.getSize());
            laptop.setMaterial_type(laptopDTO.getMaterialType());
            laptop.setBattery_system(laptopDTO.getBatterySystem());
            laptop.setOs_system(laptopDTO.getOsSystem());
            laptop.setRelease_time(laptopDTO.getReleaseTime());
            laptop.setConnectivity_ports(laptopDTO.getConnectivityPorts());
            laptop.setWireless(laptopDTO.getWireless());
            laptop.setWebcams(laptopDTO.getWebcams());
            laptop.setFeatures(laptopDTO.getFeatures());
            laptop.setKeyboard_lights(laptopDTO.getKeyboardLights());
            laptop.setRam_type(laptopDTO.getRamType());
            laptop.setRam_speed(laptopDTO.getRamSpeed());
            laptop.setMax_ram_size(laptopDTO.getMaxRamSize());
            laptop.setHard_drive(laptopDTO.getHardDrive());
            laptop.setReview(laptopDTO.getReview());
            laptop.setScreen_type(laptopDTO.getScreenType());
            laptop.setResolution(laptopDTO.getResolution());
            laptop.setRefresh_rate(laptopDTO.getRefreshRate());
            laptop.setColor_depth(laptopDTO.getColorDepth());
            laptop.setScreen_technology(laptopDTO.getScreenTechnology());
            laptop.setScreen_card(laptopDTO.getScreenCard());
            laptop.setSound_technology(laptopDTO.getSoundTechnology());
            laptop.setDeleted(false);

            List<String> imageUrls = new ArrayList<>();
            String uploadDir = "src/main/resources/static/laptop_img/";

            if (imageFiles != null) {
                for (MultipartFile imageFile : imageFiles) {
                    if (!imageFile.isEmpty()) {
                        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                        Path path = Paths.get(uploadDir + fileName);
                        Files.createDirectories(path.getParent());
                        Files.write(path, imageFile.getBytes());

                        imageUrls.add("/laptop_img/" + fileName);
                    }
                }
            }

            // Nếu không có ảnh nào được upload, thêm ảnh mặc định
            if (imageUrls.isEmpty()) {
                imageUrls.add("/img/default_laptop.jpg");
            }

            laptop.setImage_url(String.join(" | ", imageUrls));

            laptopRepository.save(laptop);

            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    @Transactional
    public boolean editLaptop(Long id, LaptopDTO laptopDTO, MultipartFile[] imageFiles) {
        Optional<Laptop> optionalLaptop = laptopRepository.findById(id);
        if (optionalLaptop.isEmpty() || laptopDTO == null) {
            return false;
        }

        Laptop laptop = optionalLaptop.get();

        try {

            laptop.setName(laptopDTO.getName());
            laptop.setModel(laptopDTO.getModel());
            laptop.setCpu_name(laptopDTO.getCpuName());
            laptop.setCores(laptopDTO.getCores());
            laptop.setThreads(laptopDTO.getThreads());
            laptop.setBase_speed(laptopDTO.getBaseSpeed());
            laptop.setMax_speed(laptopDTO.getMaxSpeed());
            laptop.setOriginal_price(laptopDTO.getOriginalPrice());
            laptop.setCurrent_price(laptopDTO.getCurrentPrice());
            laptop.setDiscount(laptopDTO.getDiscount());
            laptop.setDescription(laptopDTO.getDescription());
            laptop.setSize(laptopDTO.getSize());
            laptop.setMaterial_type(laptopDTO.getMaterialType());
            laptop.setBattery_system(laptopDTO.getBatterySystem());
            laptop.setOs_system(laptopDTO.getOsSystem());
            laptop.setRelease_time(laptopDTO.getReleaseTime());
            laptop.setConnectivity_ports(laptopDTO.getConnectivityPorts());
            laptop.setWireless(laptopDTO.getWireless());
            laptop.setWebcams(laptopDTO.getWebcams());
            laptop.setFeatures(laptopDTO.getFeatures());
            laptop.setKeyboard_lights(laptopDTO.getKeyboardLights());
            laptop.setRam_type(laptopDTO.getRamType());
            laptop.setRam_speed(laptopDTO.getRamSpeed());
            laptop.setMax_ram_size(laptopDTO.getMaxRamSize());
            laptop.setHard_drive(laptopDTO.getHardDrive());
            laptop.setReview(laptopDTO.getReview());
            laptop.setScreen_type(laptopDTO.getScreenType());
            laptop.setResolution(laptopDTO.getResolution());
            laptop.setRefresh_rate(laptopDTO.getRefreshRate());
            laptop.setColor_depth(laptopDTO.getColorDepth());
            laptop.setScreen_technology(laptopDTO.getScreenTechnology());
            laptop.setScreen_card(laptopDTO.getScreenCard());
            laptop.setSound_technology(laptopDTO.getSoundTechnology());

            // Xử lý ảnh mới nếu có
            if (imageFiles != null && imageFiles.length > 0) {
                List<String> imageUrls = new ArrayList<>();
                String uploadDir = "src/main/resources/static/uploads/laptops/";

                for (MultipartFile imageFile : imageFiles) {
                    if (!imageFile.isEmpty()) {
                        String fileName = System.currentTimeMillis() + "_" + imageFile.getOriginalFilename();
                        Path path = Paths.get(uploadDir + fileName);
                        Files.createDirectories(path.getParent());
                        Files.write(path, imageFile.getBytes());
                        imageUrls.add("/uploads/laptops/" + fileName);
                    }
                }

                // Nếu có ít nhất 1 ảnh được upload thì cập nhật lại ảnh
                if (!imageUrls.isEmpty()) {
                    laptop.setImage_url(String.join(" | ", imageUrls));
                }
            }

            laptopRepository.save(laptop);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    @Transactional
    public boolean deleteLaptop(Long id) {
        Optional<Laptop> optionalLaptop = laptopRepository.findById(id);
        if (optionalLaptop.isEmpty()) {
            return false;
        }

        try {
            Laptop laptop = optionalLaptop.get();
            laptop.setDeletedAt(LocalDateTime.now());
            laptop.setDeleted(true); // Xóa mềm
            laptopRepository.save(laptop);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<Laptop> getAllLaptops() {
        return laptopRepository.findByDeletedFalse();
    }

    @Override
    public Optional<Laptop> getLaptopById(Long id) {
        return laptopRepository.findById(id);
    }
    @Scheduled(cron = "0 0 0 * * ?")
    @Transactional
    public void permanentlyDeleteOldLaptops() {
        LocalDateTime oneMonthAgo = LocalDateTime.now().minusMonths(1);
        List<Laptop> oldDeletedLaptops = laptopRepository.findAllByDeletedTrueAndRestoredFalseAndDeletedAtBefore(oneMonthAgo);

        for (Laptop laptop : oldDeletedLaptops) {
            laptopRepository.delete(laptop);
        }
    }
    public List<Laptop> getDeletedLaptops() {
        return laptopRepository.findAllByDeletedTrue();
    }

    @Transactional
    public boolean restoreLaptop(Long id) {
        Optional<Laptop> optionalLaptop = laptopRepository.findById(id);
        if (optionalLaptop.isEmpty()) {
            return false;
        }

        try {
            Laptop laptop = optionalLaptop.get();
            if (!laptop.isDeleted()) {
                return false;
            }

            laptop.setDeleted(false);
            laptop.setDeletedAt(null);
            laptop.setRestored(true); // Đánh dấu đã được khôi phục
            laptopRepository.save(laptop);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    public List<Map<String, Object>> getLaptopStockStats() {
        List<Object[]> results = laptopRepository.countByModel();
        List<Map<String, Object>> stats = new ArrayList<>();
        for (Object[] row : results) {
            Map<String, Object> map = new HashMap<>();
            map.put("model", row[0]);
            map.put("stock", row[1]);
            stats.add(map);
        }
        return stats;
    }
    public List<Map<String, Object>> getStockByDiscount() {
        List<Object[]> results = laptopRepository.stockByDiscount();
        return results.stream().map(obj -> {
            Map<String, Object> map = new HashMap<>();
            map.put("discount", obj[0]);
            map.put("stock", obj[1]);
            return map;
        }).collect(Collectors.toList());
    }


}
