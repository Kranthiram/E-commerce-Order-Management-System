package com.ecommerce.order_management.service.discount;

public interface DiscountStrategy {

    double applyDiscount(double originalPrice);

    double getDiscountPercentage();
}
