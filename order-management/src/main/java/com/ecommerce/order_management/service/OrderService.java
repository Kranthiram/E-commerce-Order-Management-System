package com.ecommerce.order_management.service;

import com.ecommerce.order_management.entity.*;
import com.ecommerce.order_management.repository.OrderItemRepository;
import com.ecommerce.order_management.repository.OrderRepository;
import com.ecommerce.order_management.service.discount.DiscountStrategy;
import com.ecommerce.order_management.service.discount.DiscountStrategyFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductService productService;
    private final UserService userService;
    private final DiscountStrategyFactory discountStrategyFactory;

    // Spring injects ALL of these automatically
    @Autowired
    public OrderService(OrderRepository orderRepository,
                        OrderItemRepository orderItemRepository,
                        ProductService productService,
                        UserService userService,
                        DiscountStrategyFactory discountStrategyFactory) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.productService = productService;
        this.userService = userService;
        this.discountStrategyFactory = discountStrategyFactory;
    }

    @Transactional
    public Order placeOrder(Long userId, Map<Long, Integer> productQuantities) {
        // Step 1 — get the user
        User user = userService.getUserById(userId);

        // Step 2 — calculate raw total from products
        double rawTotal = 0.0;
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productService.getProductById(entry.getKey());
            rawTotal += product.getPrice() * entry.getValue();
        }

        // Step 3 — Factory gives us the right strategy for this user type
        //           Strategy calculates the final price
        DiscountStrategy strategy = discountStrategyFactory.getStrategy(user.getUserType());
        double finalTotal = strategy.applyDiscount(rawTotal);

        // Step 4 — create the Order object
        Order order = new Order();
        order.setUser(user);
        order.setStatus(OrderStatus.PENDING);
        order.setTotalAmount(finalTotal);
        order.setCreatedAt(LocalDateTime.now());

        // Step 5 — save order first to get its generated ID
        Order savedOrder = orderRepository.save(order);

        // Step 6 — create OrderItems and reduce stock
        for (Map.Entry<Long, Integer> entry : productQuantities.entrySet()) {
            Product product = productService.getProductById(entry.getKey());
            int quantity = entry.getValue();

            // Reduce stock — throws exception if insufficient
            productService.reduceStock(product.getId(), quantity);

            // Create order item
            OrderItem item = new OrderItem();
            item.setOrder(savedOrder);
            item.setProduct(product);
            item.setQuantity(quantity);
            item.setPriceAtPurchase(product.getPrice()); // snapshot of price
            orderItemRepository.save(item);
        }

        return savedOrder;
    }

    @Transactional
    public Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + orderId));
    }

    @Transactional
    public List<Order> getOrdersByUser(Long userId) {
        return orderRepository.findByUserId(userId);
    }

    @Transactional
    public Order updateOrderStatus(Long orderId, OrderStatus newStatus) {
        Order order = getOrderById(orderId);

        // Business rule — cancelled orders cannot be updated
        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException("Cannot update a cancelled order");
        }

        order.setStatus(newStatus);
        return orderRepository.save(order);
    }

    @Transactional
    public Order cancelOrder(Long orderId) {
        Order order = getOrderById(orderId);

        // Business rule — only PENDING orders can be cancelled
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new RuntimeException(
                    "Only PENDING orders can be cancelled. Current status: "
                            + order.getStatus());
        }

        order.setStatus(OrderStatus.CANCELLED);
        return orderRepository.save(order);
    }

    @Transactional
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }
}
