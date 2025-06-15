package com.example.laptopshop.controller;

import com.example.laptopshop.dto.OrderRequest;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.service.OrderService;
import com.example.laptopshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

    @RestController
    @RequestMapping("/api")
    public class OrderApiController {
        @Autowired
        private OrderService orderService;

        @Autowired
        private UserService userService;

        @PostMapping("/orders")
        public ResponseEntity<?> createOrder(@RequestBody OrderRequest request) {
            User user = userService.getCurrentUser();
            if (user == null) {
                return ResponseEntity.status(401).body("Bạn cần đăng nhập để đặt hàng.");
            }

            orderService.placeOrder(user, request.getAddress(), request.getPhone());
            return ResponseEntity.ok("Đặt hàng thành công");
        }
    }


