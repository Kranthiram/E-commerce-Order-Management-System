package com.ecommerce.order_management.controller;

import com.ecommerce.order_management.dto.OrderDTO;
import com.ecommerce.order_management.dto.OrderItemDTO;
import com.ecommerce.order_management.dto.PlaceOrderRequest;
import com.ecommerce.order_management.entity.Order;
import com.ecommerce.order_management.entity.OrderItem;
import com.ecommerce.order_management.entity.OrderStatus;
import com.ecommerce.order_management.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    @Autowired
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    // Convert OrderItem entity → DTO
    private OrderItemDTO toItemDTO(OrderItem item) {
        return new OrderItemDTO(
                item.getId(),
                item.getProduct().getId(),
                item.getProduct().getName(),
                item.getQuantity(),
                item.getPriceAtPurchase()
        );
    }

    // Convert Order entity → DTO
    private OrderDTO toDTO(Order order) {
        List<OrderItemDTO> itemDTOs = order.getOrderItems() == null
                ? List.of()
                : order.getOrderItems()
                .stream()
                .map(this::toItemDTO)
                .collect(Collectors.toList());

        return new OrderDTO(
                order.getId(),
                order.getStatus().name(),
                order.getTotalAmount(),
                order.getCreatedAt(),
                order.getUser().getId(),
                order.getUser().getName(),
                itemDTOs
        );
    }

    // POST /api/orders
    @PostMapping
    public ResponseEntity<OrderDTO> placeOrder(
            @RequestBody PlaceOrderRequest request) {
        Order order = orderService.placeOrder(
                request.getUserId(),
                request.getProductQuantities()
        );
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(order));
    }

    // GET /api/orders/5
    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Long id) {
        return ResponseEntity.ok(toDTO(orderService.getOrderById(id)));
    }

    // GET /api/orders/user/3
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByUser(
            @PathVariable Long userId) {
        List<OrderDTO> orders = orderService.getOrdersByUser(userId)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    // GET /api/orders
    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(orders);
    }

    // PATCH /api/orders/5/status?newStatus=CONFIRMED
    @PatchMapping("/{id}/status")
    public ResponseEntity<OrderDTO> updateStatus(
            @PathVariable Long id,
            @RequestParam OrderStatus newStatus) {
        Order updated = orderService.updateOrderStatus(id, newStatus);
        return ResponseEntity.ok(toDTO(updated));
    }

    // PATCH /api/orders/5/cancel
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderDTO> cancelOrder(@PathVariable Long id) {
        Order cancelled = orderService.cancelOrder(id);
        return ResponseEntity.ok(toDTO(cancelled));
    }
}