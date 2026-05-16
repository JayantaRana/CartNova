package com.example.jwtDemo.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.jwtDemo.entity.PurchaseOrder;
import com.example.jwtDemo.entity.User;

//new add
import java.util.List; // 🔴 Make sure to add this import

public interface PurchaseOrderRepository extends JpaRepository<PurchaseOrder, Long> {
    Optional<PurchaseOrder> findByIdAndUser(Long id, User user);

    //new add for user order
    List<PurchaseOrder> findByUser(User user);
}
