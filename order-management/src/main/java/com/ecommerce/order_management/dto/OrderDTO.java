package com.ecommerce.order_management.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private String status;
    private Double totalAmount;
    private LocalDateTime createdAt;
    private Long userId;
    private String userName;
    private List<OrderItemDTO> items;
}
