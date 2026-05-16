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

    // Inject your OrderService here to talk to your database
    // private final OrderService orderService;
    // public CustomerOrderController(OrderService orderService) { this.orderService = orderService; }

    @GetMapping
    public ResponseEntity<?> getUserOrderHistory(Principal principal) {
        // 'principal.getName()' gives you the username/email extracted directly from your verified JWT token
        String username = principal.getName();
        System.out.println("Fetching order history records for customer username: " + username);
        
        // Fetch matching database records using your service layers:
        // List<OrderResponse> orders = orderService.getOrdersByUsername(username);
        // return ResponseEntity.ok(orders);
        
        // Temporary placeholder to verify the 403 error disappears:
        return ResponseEntity.ok(List.of()); 
    }
}
