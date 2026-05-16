package com.example.jwtDemo.controller;

import com.example.jwtDemo.entity.PurchaseOrder;
import com.example.jwtDemo.entity.OrderItem;
import com.example.jwtDemo.entity.User;
import com.example.jwtDemo.repository.PurchaseOrderRepository;
import com.example.jwtDemo.repository.OrderItemRepository;
import com.example.jwtDemo.repository.UserRepository; // 🔴 Import your UserRepository

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("/customer/orders")
public class CustomerOrderController {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final OrderItemRepository orderItemRepository;
    private final UserRepository userRepository; // To fetch the User object by username

    // Inject all three repositories
    public CustomerOrderController(PurchaseOrderRepository purchaseOrderRepository, 
                                   OrderItemRepository orderItemRepository, 
                                   UserRepository userRepository) {
        this.purchaseOrderRepository = purchaseOrderRepository;
        this.orderItemRepository = orderItemRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public ResponseEntity<List<Map<String, Object>>> getUserOrderHistory(Principal principal) {
        if (principal == null) {
            return ResponseEntity.status(401).build();
        }

        // 1. Get username string from JWT token
        String username = principal.getName();

        // 2. Fetch the real User entity from your database using the username string
        User user = userRepository.findByUsername(username)
                .orElse(null); // (Change findByUsername to findByEmail if your field is named email)

        if (user == null) {
            return ResponseEntity.status(404).build(); // User not found in database
        }

        // 3. Find all orders belonging to this specific User entity
        List<PurchaseOrder> dbOrders = purchaseOrderRepository.findByUser(user);

        List<Map<String, Object>> completeResponse = new ArrayList<>();

        // 4. Loop through each order to get its items
        for (PurchaseOrder order : dbOrders) {
            Map<String, Object> orderMap = new HashMap<>();
            orderMap.put("orderId", order.getId());
            orderMap.put("totalAmount", order.getTotalAmount());
            orderMap.put("orderStatus", order.getStatus() != null ? order.getStatus().toUpperCase() : "SUCCESS");
            orderMap.put("orderDate", "2026-05-16T12:00:00Z"); // Fallback date representation

            // 5. Find all order items belonging to this specific PurchaseOrder entity
            List<OrderItem> itemsForThisOrder = orderItemRepository.findByOrder(order);
            List<Map<String, Object>> serializableItemsList = new ArrayList<>();

            for (OrderItem item : itemsForThisOrder) {
                Map<String, Object> itemMap = new HashMap<>();
                itemMap.put("orderItemId", item.getId());
                itemMap.put("quantity", item.getQuantity());
                itemMap.put("price", item.getPriceAtPurchase());
                
                if (item.getProduct() != null) {
                    itemMap.put("productName", item.getProduct().getName());
                    itemMap.put("category", "E-Commerce");
                    itemMap.put("imageUrl", item.getProduct().getImageUrl() != null ? item.getProduct().getImageUrl() : "https://images.unsplash.com/photo-1523275335684-37898b6baf30");
                } else {
                    itemMap.put("productName", "CartNova Product");
                    itemMap.put("category", "General");
                    itemMap.put("imageUrl", "https://images.unsplash.com/photo-1523275335684-37898b6baf30");
                }
                serializableItemsList.add(itemMap);
            }

            orderMap.put("items", serializableItemsList);
            completeResponse.add(orderMap);
        }

        return ResponseEntity.ok(completeResponse);
    }
}
