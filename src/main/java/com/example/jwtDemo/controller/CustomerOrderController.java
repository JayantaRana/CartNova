package com.example.jwtDemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.security.Principal;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/customer/orders")
public class CustomerOrderController {

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getUserOrderHistory(Principal principal) {
        // If the token is valid, Spring Security populates this 'principal' object automatically
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        // Extract the logged-in user's email/username directly from the JWT
        String username = principal.getName();
        System.out.println("Generating mock orders for logged-in user: " + username);

        // Create a fake, structured order that matches exactly what your React UI expects
        Map<String, Object> mockOrder = Map.of(
            "orderId", 98765,
            "orderDate", "2026-05-16T11:30:00Z",
            "totalAmount", 2499,
            "orderStatus", "DELIVERED",
            "items", List.of(
                Map.of(
                    "orderItemId", 101,
                    "productName", "Premium Leather Wallet (Mock Data for " + username + ")",
                    "category", "Accessories",
                    "quantity", 1,
                    "price", 2499,
                    "subtotal", 2499,
                    "imageUrl", "https://images.unsplash.com/photo-1627123424574-724758594e93"
                )
            )
        );

        // Return the list containing our mock order
        return ResponseEntity.ok(List.of(mockOrder)); 
    }
}
