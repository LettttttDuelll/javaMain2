package com.example.laptopshop.controller;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import com.example.laptopshop.dto.LaptopDTO;
import com.example.laptopshop.service.LaptopServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.example.laptopshop.entity.Laptop;
@RestController
@RequestMapping("/api/v1/laptops")
public class LaptopController {
    @Autowired
    private LaptopServiceImpl laptopService;

    @Autowired
    private com.example.laptopshop.repository.LaptopRepository laptopRepository;

    @GetMapping("/list")
    public ResponseEntity<List<Laptop>> getAllLaptop() {
        List<Laptop>  laptops = laptopService.getAllLaptops();
        Collections.reverse(laptops);
        return ResponseEntity.ok(laptops);
    }
    @GetMapping("/laptop/{id}")
    public ResponseEntity<Optional<Laptop>> getLaptopById(@PathVariable Long id) {
        Optional<Laptop> laptop = laptopService.getLaptopById(id);
        if (laptop.isPresent()) {
            return ResponseEntity.ok(laptop);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
    @PostMapping(value = "/add" ,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<String> addLaptop(
            @RequestPart("laptop") LaptopDTO laptopDTO,
            @RequestPart(value = "imageFiles", required = false) MultipartFile[] imageFiles) {
        System.out.println("Số ảnh nhận được: " + imageFiles.length);
        boolean success = laptopService.addLaptop(laptopDTO,imageFiles);
        if (success) {
            return ResponseEntity.ok("Thêm laptop thành công");
        } else {
            return ResponseEntity.badRequest().body("Thêm thất bại, dữ liệu thiếu hoặc không hợp lệ");
        }
    }
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editLaptop(@PathVariable Long id, @RequestPart("laptop") LaptopDTO laptopDTO,
                                             @RequestParam(value = "imageFile", required = false) MultipartFile[] imageFile) {
        boolean success = laptopService.editLaptop(id, laptopDTO , imageFile);
        if (success) {
            return ResponseEntity.ok("Cập nhật laptop thành công");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy laptop hoặc dữ liệu sai");
        }
    }

    // 5. Xóa laptop
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLaptop(@PathVariable Long id) {
        boolean success = laptopService.deleteLaptop(id);
        if (success) {
            return ResponseEntity.ok("Đã xóa laptop thành công");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy laptop");
        }
    }

    @PutMapping("/restore/{id}")
    public ResponseEntity<String> restoreLaptop(@PathVariable Long id) {
        boolean restored = laptopService.restoreLaptop(id);
        if (restored) {
            return ResponseEntity.ok("Khôi phục laptop thành công");
        } else {
            return ResponseEntity.badRequest().body("Laptop không tồn tại hoặc chưa bị xóa");
        }
    }
    @GetMapping("/trash")
    public ResponseEntity<List<Laptop>> getDeletedLaptops() {
        List<Laptop> deletedLaptops = laptopService.getDeletedLaptops();
        return ResponseEntity.ok(deletedLaptops);
    }

    @PutMapping("/edit-stock/{id}")
    public ResponseEntity<?> updateStock(
        @PathVariable Long id,
        @RequestBody Map<String, Integer> body) {

    Laptop laptop = laptopRepository.findById(id).orElseThrow();

    laptop.setStock(body.get("stock"));
    laptopRepository.save(laptop);

    return ResponseEntity.ok("Updated");
}

@GetMapping("/search")
public ResponseEntity<List<Laptop>> searchLaptop(
        @RequestParam(required = false) String keyword,
        @RequestParam(required = false) String brand,
        @RequestParam(required = false) Double minPrice,
        @RequestParam(required = false) Double maxPrice,
        @RequestParam(required = false) String sort
) {

    List<Laptop> laptops = laptopRepository.searchAndFilter(
            keyword,
            brand,
            minPrice,
            maxPrice
    );

    // SORT
    if ("priceAsc".equals(sort)) {
        laptops.sort((a,b) ->
                Double.compare(a.getCurrent_price(), b.getCurrent_price()));
    }
    else if ("priceDesc".equals(sort)) {
        laptops.sort((a,b) ->
                Double.compare(b.getCurrent_price(), a.getCurrent_price()));
    }
    else if ("nameAsc".equals(sort)) {
        laptops.sort((a,b) ->
                a.getName().compareToIgnoreCase(b.getName()));
    }
    else if ("nameDesc".equals(sort)) {
        laptops.sort((a,b) ->
                b.getName().compareToIgnoreCase(a.getName()));
    }

    return ResponseEntity.ok(laptops);
}
}
