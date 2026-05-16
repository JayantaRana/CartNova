package com.example.jwtDemo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/customer/orders")
public class CustomerOrderController {

    @GetMapping
    public ResponseEntity<List<Object>> getUserOrderHistory() {
        // This safely returns HTTP 200 OK with [] without needing any other files
        return ResponseEntity.ok(List.of()); 
    }
}
