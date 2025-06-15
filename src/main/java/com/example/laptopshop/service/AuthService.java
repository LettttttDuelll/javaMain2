package com.example.laptopshop.service;

import com.example.laptopshop.dto.LoginRequest;
import com.example.laptopshop.dto.RegisterRequest;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.repository.AuthRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AuthService implements AuthServiceInterface{
    private final AuthRepository authRepository;
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    // Constructor Injection
    @Autowired
    public AuthService(AuthRepository authRepository) {
        this.authRepository = authRepository;
    }
    @Transactional
    public boolean RegisterUser (RegisterRequest req){
        if( authRepository.existsByUsername(req.getUsername())) {
            return false ;
        }
        User user = new User();
        user.setUsername(req.getUsername());
        if(req.getPassword().equals(req.getConfirmPassword())) {
          return false;
        }
        String password = passwordEncoder.encode(req.getPassword());
        user.setPassword(password);
        user.setEmail(req.getEmail());
        user.setCreatedDate(LocalDateTime.now());
        user.setRoles("ROLE_CUSTOMER");
        user.setLogined(false);
        authRepository.save(user);
        return true;
    }
    @Transactional
    public boolean LoginUser(LoginRequest req){
        Optional<User> userOpt = authRepository.findByUsername(req.getUsername());
        if(userOpt.isEmpty()) {
            return false ;
        }
        User user = userOpt.get();
        if(!passwordEncoder.matches(req.getPassword(), user.getPassword())){
            return false;
        }
        user.setLogined(true);
        user.setLastLogined(LocalDateTime.now());
        return true;
    }

    public Optional<User> findByUsername(String username) {
        return authRepository.findByUsername(username);
    }
}
