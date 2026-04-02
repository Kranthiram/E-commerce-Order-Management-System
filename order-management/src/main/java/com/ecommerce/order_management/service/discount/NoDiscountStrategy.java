package com.ecommerce.order_management.service.discount;

public class NoDiscountStrategy implements DiscountStrategy{

    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice;
    }

    @Override
    public double getDiscountPercentage() {
        return 0.0;
    }
}
