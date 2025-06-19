package com.example.laptopshop.controller;

import com.example.laptopshop.dto.LoginRequest;
import com.example.laptopshop.dto.RegisterRequest;
import com.example.laptopshop.entity.Laptop;
import com.example.laptopshop.entity.Order;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.service.LaptopServiceImpl;
import com.example.laptopshop.service.OrderService;
import com.example.laptopshop.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Controller
public class ViewController {

    @Autowired
    private UserService userService;

    @Autowired
    private LaptopServiceImpl laptopService;

    @Autowired
    private OrderService orderService;

    // Trang đăng nhập
    @GetMapping("/custom-login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login/login";
    }

    // Trang dashboard chính
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/dashboard";
    }

    // Trang đăng ký
    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "login/register";
    }

    // Danh sách người dùng
    @GetMapping("/user_list")
    public String listUsers(Model model) {
        List<User> users = userService.getAllUsers();
        Collections.reverse(users);
        model.addAttribute("users", users);
        return "dashboard/list_user";
    }

    // Thùng rác (user đã xoá)
    @GetMapping("/bin")
    public String bin() {
        return "dashboard/bin";
    }

    // Giỏ hàng của người dùng
    @GetMapping("/cart")
    public String cart() {
        return "end_user/cart";
    }

    // Trang chủ cho người dùng cuối
    @GetMapping("/home")
    public String home(Model model) {
        List<Laptop> laptops = laptopService.getAllLaptops();
        model.addAttribute("products", laptops);
        return "end_user/home";
    }

    // Trang chi tiết sản phẩm
    @GetMapping("/detailproduct")
    public String detailProduct(@RequestParam("id") Long productId, Model model) {
        Optional<Laptop> laptop = laptopService.getLaptopById(productId);
        laptop.ifPresent(l -> model.addAttribute("laptop", l));
        return "end_user/detailproduct";
    }

    // Trang đặt hàng
    @GetMapping("/order")
    public String order() {
        return "end_user/order";
    }

    // Danh sách sản phẩm trong dashboard
    @GetMapping("/productList")
    public String productList(Model model) {
        List<Laptop> laptops = laptopService.getAllLaptops();
        Collections.reverse(laptops);
        model.addAttribute("laptops", laptops);
        return "dashboard/DsType";
    }

    // Danh sách đơn hàng dành cho admin
    @GetMapping("/admin/orderList")
    public String orderList(Model model) {
        List<Order> orders = orderService.getAllOrders();
        Collections.reverse(orders);
        model.addAttribute("list", orders);
        return "dashboard/list_order";
    }
}
