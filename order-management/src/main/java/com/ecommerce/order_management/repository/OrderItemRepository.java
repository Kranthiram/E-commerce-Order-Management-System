package com.ecommerce.order_management.repository;

import com.ecommerce.order_management.entity.OrderItem;
import com.ecommerce.order_management.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderItemRepository extends JpaRepository<OrderItem,Long> {
    List<OrderItem> findByOrderId(Long OrderId);
}
