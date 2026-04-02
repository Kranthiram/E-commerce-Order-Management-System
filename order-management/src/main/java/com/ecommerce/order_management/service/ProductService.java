package com.ecommerce.order_management.service;

import com.ecommerce.order_management.entity.Product;
import com.ecommerce.order_management.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    @Autowired
    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(Product product) {
        return productRepository.save(product);
    }

    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }

    public Product getProductById(Long id) {
        // Optional.orElseThrow() — if product not found, throw exception
        // Instead of returning null and causing NullPointerException later
        return productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + id));
    }

    public List<Product> searchProducts(String name) {
        return productRepository.findByNameContainingIgnoreCase(name);
    }

    public Product updateProduct(Long id, Product updatedProduct) {
        // First verify product exists
        Product existing = getProductById(id);

        // Update only the fields that should change
        existing.setName(updatedProduct.getName());
        existing.setDescription(updatedProduct.getDescription());
        existing.setPrice(updatedProduct.getPrice());
        existing.setStockQuantity(updatedProduct.getStockQuantity());

        // save() does UPDATE if id exists, INSERT if id is new
        return productRepository.save(existing);
    }

    public void deleteProduct(Long id) {
        // Verify exists before deleting
        getProductById(id);
        productRepository.deleteById(id);
    }

    // Called by OrderService to reduce stock when order is placed
    public void reduceStock(Long productId, int quantity) {
        Product product = getProductById(productId);

        if (product.getStockQuantity() < quantity) {
            throw new RuntimeException("Insufficient stock for product: "
                    + product.getName());
        }

        product.setStockQuantity(product.getStockQuantity() - quantity);
        productRepository.save(product);
    }
}