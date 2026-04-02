package com.ecommerce.order_management.service.discount;

public class PremiumDiscountStrategy implements DiscountStrategy{

    private static final double DISCOUNT_RATE = 0.10;
    @Override
    public double applyDiscount(double originalPrice) {
        return originalPrice - (originalPrice * DISCOUNT_RATE);
    }

    @Override
    public double getDiscountPercentage() {
        return DISCOUNT_RATE * 100;
    }
}
