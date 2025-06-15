package com.example.laptopshop.controller;

import com.example.laptopshop.dto.LoginRequest;
import com.example.laptopshop.dto.RegisterRequest;
import com.example.laptopshop.entity.Laptop;
import com.example.laptopshop.entity.Order;
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
    private OrderService ordersService;

    @GetMapping("/custom-login")
    public String login(Model model) {
        model.addAttribute("loginRequest", new LoginRequest());
        return "login/login";
    }

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard/dashboard";
    }

    @GetMapping("/dashboard/users")
    public String listUser() {
        return "dashboard/list_user";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("registerRequest", new RegisterRequest());
        return "login/register";
    }

    @GetMapping("/user_list")
    public String listUsers(Model model) {
        List<com.example.laptopshop.entity.User> users = userService.getAllUsers();
        Collections.reverse(users);
        model.addAttribute("users", users);
        return "dashboard/list_user";
    }

    @GetMapping("/bin")
    public String bin() {
        return "dashboard/bin";
    }

    @GetMapping("/cart")
    public String cart() {
        return "end_user/cart";
    }

    @GetMapping("/home")
    public String home(Model model) {
        List<Laptop> laptops = laptopService.getAllLaptops();
        model.addAttribute("products", laptops);
        return "end_user/home";
    }

    @GetMapping("/detailproduct")
    public String detailProduct(@RequestParam("id") Long laptopId, Model model) {
        Optional<Laptop> laptops = laptopService.getLaptopById(laptopId);
        laptops.ifPresent(laptop -> model.addAttribute("laptop", laptop));
        return "end_user/detailproduct";
    }

    @GetMapping("/order")
    public String order() {
        return "end_user/order";
    }

    @GetMapping("/productList")
    public String productList(Model model) {
        List<Laptop> laptops = laptopService.getAllLaptops();
        Collections.reverse(laptops);
        model.addAttribute("laptops", laptops);
        return "dashboard/DsType";
    }

    @GetMapping("/admin/orderList")
    public String orderList(Model model) {
        List<Order> orders = ordersService.getAllOrders();
        Collections.reverse(orders);
        model.addAttribute("list", orders);
        return "dashboard/list_order";
    }
}
