package com.ecommerce.order_management.repository;

import com.ecommerce.order_management.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    List<Product> findByNameContainingIgnoreCase(String name);
    //This method name is equalent to "SELECT * FROM products WHERE LOWER(name) LIKE LOWER('%value%')"

    List<Product> findByStockQuantityGreaterThan(Integer quantity);
    // Spring from the given method name generates this Query "SELECT * FROM products WHERE stock_quantity > ? "



}
