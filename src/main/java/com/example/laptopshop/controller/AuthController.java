package com.example.laptopshop.controller;

import com.example.laptopshop.dto.LoginRequest;
import com.example.laptopshop.dto.RegisterRequest;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.service.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {

        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest request) {
        boolean success = authService.RegisterUser(request);
        if (success) {
            return ResponseEntity.ok("Đăng ký thành công");
        } else {
            return ResponseEntity.badRequest().body("Tên đăng nhập đã tồn tại");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request , HttpServletRequest httpRequest) {
        boolean success = authService.LoginUser(request);
        if (!success) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("message", "Sai tài khoản hoặc mật khẩu"));
        }

        Optional<User> userOpt = authService.findByUsername(request.getUsername());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("message", "Không tìm thấy người dùng"));
        }
        User user = userOpt.get();
        // Tạo Authentication
        List<SimpleGrantedAuthority> authorities = Arrays.stream(user.getRoles().split(","))
                .map(String::trim)
                .map(role -> {
                    String r = role.startsWith("ROLE_") ? role : "ROLE_" + role;
                    return new SimpleGrantedAuthority(r);
                })
                .toList();
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(user.getUsername(), null, authorities);

        SecurityContextHolder.getContext().setAuthentication(authToken);

        // Tạo session để lưu Authentication
        httpRequest.getSession(true)
                .setAttribute("SPRING_SECURITY_CONTEXT", SecurityContextHolder.getContext());

        return ResponseEntity.ok(Map.of(
                "message", "Đăng nhập thành công",
                "role", user.getRoles()
        ));
    }
}
