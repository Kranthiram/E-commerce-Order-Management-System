package com.ecommerce.order_management.service.discount;

import org.springframework.stereotype.Component;

@Component
public class DiscountStrategyFactory {
    public DiscountStrategy getStrategy(String userType){
        if(userType == null){
            return new  NoDiscountStrategy();
        }
        return switch (userType.toUpperCase()) {
            case "PREMIUM" -> new PremiumDiscountStrategy();
            case "EMPLOYEE" -> new EmployeeDiscountStrategy();
            default -> new NoDiscountStrategy();
        };
    }
}
