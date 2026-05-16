package com.example.jwtDemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/customer/orders")
public class CustomerOrderController {

    private final OrderService orderService; // Inject your Order business logic service

    public CustomerOrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getUserOrderHistory(Principal principal) {
        // 1. Get the username/email out of your verified JWT token
        String username = principal.getName();
        
        // 2. Query your purchase_order and order_items tables based on this user
        List<OrderResponse> orders = orderService.getOrdersByUsername(username);
        
        // 3. Return the real array back to React
        return ResponseEntity.ok(orders); 
    }
}
