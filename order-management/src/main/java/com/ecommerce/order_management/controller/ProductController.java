package com.ecommerce.order_management.controller;

import com.ecommerce.order_management.dto.ProductDTO;
import com.ecommerce.order_management.entity.Product;
import com.ecommerce.order_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Convert Entity → DTO (never send raw entity to client)
    private ProductDTO toDTO(Product product) {
        return new ProductDTO(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStockQuantity()
        );
    }

    // Convert DTO → Entity (for create/update operations)
    private Product toEntity(ProductDTO dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setStockQuantity(dto.getStockQuantity());
        return product;
    }

    // GET /api/products
    @GetMapping
    public ResponseEntity<List<ProductDTO>> getAllProducts() {
        List<ProductDTO> products = productService.getAllProducts()
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    // GET /api/products/5
    @GetMapping("/{id}")
    public ResponseEntity<ProductDTO> getProductById(@PathVariable Long id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(toDTO(product));
    }

    // GET /api/products/search?name=phone
    @GetMapping("/search")
    public ResponseEntity<List<ProductDTO>> searchProducts(
            @RequestParam String name) {
        List<ProductDTO> results = productService.searchProducts(name)
                .stream()
                .map(this::toDTO)
                .collect(Collectors.toList());
        return ResponseEntity.ok(results);
    }

    // POST /api/products
    @PostMapping
    public ResponseEntity<ProductDTO> addProduct(
            @RequestBody ProductDTO productDTO) {
        Product saved = productService.addProduct(toEntity(productDTO));
        // 201 Created — correct status for resource creation
        return ResponseEntity.status(HttpStatus.CREATED).body(toDTO(saved));
    }

    // PUT /api/products/5
    @PutMapping("/{id}")
    public ResponseEntity<ProductDTO> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDTO productDTO) {
        Product updated = productService.updateProduct(id, toEntity(productDTO));
        return ResponseEntity.ok(toDTO(updated));
    }

    // DELETE /api/products/5
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
        // 200 OK with a confirmation message
        return ResponseEntity.ok("Product deleted successfully");
    }
}