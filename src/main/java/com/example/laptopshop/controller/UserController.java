package com.example.laptopshop.controller;

import com.example.laptopshop.dto.ProfileForm;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        return ResponseEntity.ok(users);
    }
    @GetMapping("/user/{id}")
    public ResponseEntity<Optional<User>> getUsers(@PathVariable Long id ) {
        Optional<User> user = userService.getUserById(id);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/add")
    public ResponseEntity<String> addUser(@RequestPart("user")  ProfileForm pf ,  @RequestParam(value = "imageUrl", required = false) MultipartFile imageFile)  {
        boolean success = userService.addUser(pf,imageFile);
        if (success) {
            return ResponseEntity.ok("Thêm người dùng thành công");
        } else {
            return ResponseEntity.badRequest().body("Dữ liệu không hợp lệ hoặc bị thiếu");
        }
    }
    // 2. Sửa user
    @PutMapping("/edit/{id}")
    public ResponseEntity<String> editUser(@PathVariable Long id , @RequestBody ProfileForm pf) {
        boolean success = userService.editUser(id,pf);
        if (success) {
            return ResponseEntity.ok("Cập nhật người dùng thành công");
        } else {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng hoặc dữ liệu sai");
        }
    }

    // 3. Xoá user
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {
        boolean success = userService.deleteUser(id);
        if (success) {
            return ResponseEntity.ok("Đã xoá người dùng");
        }else {
            return ResponseEntity.badRequest().body("Không tìm thấy người dùng");
        }
    }
}
