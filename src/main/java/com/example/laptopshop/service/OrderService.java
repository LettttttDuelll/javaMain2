package com.example.laptopshop.service;

import com.example.laptopshop.entity.*;
import com.example.laptopshop.repository.CartItemRepository;
import com.example.laptopshop.repository.LaptopRepository;
import com.example.laptopshop.repository.OrderRepository;
import org.openqa.selenium.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    @Autowired
    private CartService cartService;
    @Autowired private CartItemRepository cartRepo;
    @Autowired private OrderRepository orderRepo;
    @Autowired private LaptopRepository laptopRepo;

    @Transactional
    public Order placeOrder(User user, String address, String phone) {
        List<CartItem> cartItems = cartRepo.findByUserId(user.getId());
        if (cartItems.isEmpty()) throw new RuntimeException("Giỏ hàng rỗng!");

        double total = 0;
        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem ci : cartItems) {
            //Product p = ci.getProduct();
            Laptop p = ci.getLaptop();
            if (p.getStock() < ci.getQuantity()) {
                throw new RuntimeException("Sản phẩm '" + p.getName() + "' không đủ tồn kho!");
            }
            // Trừ kho
            p.setStock(p.getStock() - ci.getQuantity());
            laptopRepo.save(p);

            OrderItem oi = new OrderItem();
            oi.setLaptop(p);
            oi.setQuantity(ci.getQuantity());
            oi.setPrice(p.getCurrent_price());
            orderItems.add(oi);

            total += p.getCurrent_price() * ci.getQuantity();
        }

        // Tạo Order
        Order order = new Order();
        order.setUser(user);
        order.setOrderDate(LocalDateTime.now());
        order.setTotalPrice(total);
        order.setStatus("PENDING");
        order.setAddress(address);
        order.setPhone(phone);
        order.setItems(orderItems);

        for (OrderItem oi : orderItems) {
            oi.setOrder(order);
        }

        orderRepo.save(order);
        cartService.clearCart(user);

        return order;
    }

    public Order getOrders(int userId) {
        return orderRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Không tìm thấy đơn hàng"));
    }


    public List<Order> getAllOrders() {
        List<Order> list = orderRepo.findAll();
        System.out.println("Số đơn hàng: " + list.size());
        return list;
    }

    public Optional<Order> getOrderById(Integer id) {
        return orderRepo.findById(id);
    }
}

