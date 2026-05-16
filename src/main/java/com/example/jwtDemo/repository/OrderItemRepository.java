package com.example.jwtDemo.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwtDemo.entity.OrderItem;

//add for order
import com.example.jwtDemo.entity.PurchaseOrder;
import org.springframework.stereotype.Repository;
import java.util.List;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
  
  //new add for user order
  List<OrderItem> findByOrder(PurchaseOrder order);
}
