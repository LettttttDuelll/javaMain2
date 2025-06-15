package com.example.laptopshop.service;

import com.example.laptopshop.dto.LoginRequest;
import com.example.laptopshop.dto.RegisterRequest;

public interface AuthServiceInterface {
    public boolean RegisterUser (RegisterRequest req);
    public boolean LoginUser(LoginRequest req);
}
