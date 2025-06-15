package com.example.laptopshop.controller;

import com.example.laptopshop.dto.OrderRequest;
import com.example.laptopshop.entity.Order;
import com.example.laptopshop.entity.OrdersPDFExport;
import com.example.laptopshop.entity.SingleOrderExport;
import com.example.laptopshop.entity.User;
import com.example.laptopshop.repository.OrderRepository;
import com.example.laptopshop.service.OrderService;
import com.example.laptopshop.service.UserService;
import com.lowagie.text.DocumentException;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.view.RedirectView;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private UserService userService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private OrderRepository ordersRepository;

    @PostMapping("/create")
    public RedirectView createOrder(@ModelAttribute OrderRequest request) {
        User user = userService.getCurrentUser();

        orderService.placeOrder(user, request.getAddress(), request.getPhone());

        return new RedirectView("/orderconfirm");
    }

    @GetMapping("/orderconfirm")
    public String showOrderConfirm() {
        return "end_user/orderconfirm";
    }

    @GetMapping("/orders/export")
    public void exportToPdf(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        String currentTime= dateFormat.format(new Date());

        String HeaderKey = "Content-Disposition";
        String HeaderValue = "attachment; filename = orders"+currentTime +".pdf";
        response.setHeader(HeaderKey,HeaderValue);

        List<Order> listOrders= orderService.getAllOrders();
        OrdersPDFExport exporter = new OrdersPDFExport(listOrders);
        exporter.export(response);
    }

    @GetMapping("/orders/export/{id}") // single with @PathVariable
    public void exportSingleToPdf( @PathVariable Integer id, HttpServletResponse response) throws DocumentException, IOException{
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentTime= dateFormat.format(new Date());

        String HeaderKey = "Content-Disposition";
        String HeaderValue = "attachment; filename = orders #"+id+" "+currentTime +".pdf";
        response.setHeader(HeaderKey,HeaderValue);

        Order order = orderService.getOrderById(id).orElseThrow();
        SingleOrderExport exporter = new SingleOrderExport(order);
        exporter.export(response);
    }

    @PostMapping("/orders/update-status")
    public String updateOrderStatus(@RequestParam("orderId") Integer orderId,
                                    @RequestParam("status") String status) {
        Order order = ordersRepository.findById(orderId).orElse(null);
        if (order != null) {
            order.setStatus(status);
            ordersRepository.save(order);
        }
        return "redirect:/admin/orderList";
    }
}
