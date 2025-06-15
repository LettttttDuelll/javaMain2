package com.example.laptopshop.controller;

import com.example.laptopshop.dto.CartAddRequest;
import com.example.laptopshop.dto.CartItemDto;
import com.example.laptopshop.dto.CartUpdateRequest;
import com.example.laptopshop.entity.CartItem;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.service.CartService;
import com.example.laptopshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/cart/api")
public class CartController {

    @Autowired
    private CartService cartService;
    @Autowired
    private UserService userService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody CartAddRequest req) {
        User user = userService.getCurrentUser();
        cartService.addToCart(user, req.getProductId(), req.getQuantity());
        return ResponseEntity.ok("Đã thêm vào giỏ hàng");
    }

    @GetMapping
    public ResponseEntity<List<CartItemDto>> getCart() {
        User user = userService.getCurrentUser();
        return ResponseEntity.ok(cartService.getCartDto(user));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<?> removeItem(@RequestParam("productId") Integer cartItemId) {
        cartService.removeItemById(cartItemId);
        return ResponseEntity.ok("Đã xóa khỏi giỏ hàng");
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateQuantity(@RequestBody CartUpdateRequest req) {
        User user = userService.getCurrentUser();
        cartService.updateQuantity(user, req.getProductId(), req.getQuantity());
        return ResponseEntity.ok("Cập nhật thành công");
    }
}
