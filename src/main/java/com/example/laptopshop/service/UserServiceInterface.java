package com.example.laptopshop.service;

import com.example.laptopshop.dto.ProfileForm;
import com.example.laptopshop.entity.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

public interface UserServiceInterface {
    //@Query("SELECT u FROM users where u.delete = true")
    public List<User> getAllUsers();
    public Optional<User> getUserById(Long id);
    public Optional<User> findByUserName(String username);

    public boolean editUser(Long id , ProfileForm pf);
    public boolean deleteUser(Long id);
    public boolean addUser(ProfileForm pf , MultipartFile imageFile);
}
