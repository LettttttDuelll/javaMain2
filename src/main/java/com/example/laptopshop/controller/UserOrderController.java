package com.example.laptopshop.controller;

import java.util.List;


import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.laptopshop.entity.Order;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.repository.OrderRepository;
import com.example.laptopshop.repository.UserRepository;

@Controller
public class UserOrderController {
@Autowired OrderRepository orderRepository;
@Autowired UserRepository userRepository;

    @GetMapping("/my-orders")
    public String myOrders(Model model,
                           Authentication authentication) {

        // Lấy username đang login
        String username = authentication.getName();

        // Tìm user theo username
        User user = userRepository
                .findByUsername(username)
                .orElse(null);

        if (user == null) {
            return "redirect:/custom-login";
        }

        // Lấy danh sách đơn hàng của user
        List<Order> orders =
                orderRepository
                .findByUserIdOrderByOrderDateDesc(user.getId());

        model.addAttribute("orders", orders);

        return "end_user/my-orders";
    }

    @PostMapping("/my-orders/cancel")
public String cancelOrder(@RequestParam Integer orderId,
                          Authentication authentication) {

    String username = authentication.getName();

        // Tìm user theo username
        User user = userRepository
                .findByUsername(username)
                .orElse(null);

    Long userId = user.getId();

    Order order = orderRepository.findById(orderId).orElse(null);

    if(order != null
        && order.getUser().getId().equals(userId)
        && order.getStatus().equals("PENDING")) {

        order.setStatus("CANCELLED");

        orderRepository.save(order);
    }

    return "redirect:/my-orders";
}
}

