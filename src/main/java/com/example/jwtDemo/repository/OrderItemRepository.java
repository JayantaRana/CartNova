package com.example.jwtDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwtDemo.entity.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  //new add for user order
  List<OrderItem> findByOrderId(Long orderId);
}
