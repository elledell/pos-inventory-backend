package com.inventory1.system.controller;

import com.inventory1.system.entity.Product;
import com.inventory1.system.entity.ServiceItem;
import com.inventory1.system.repository.ProductRepository;
import com.inventory1.system.repository.ServiceItemRepository;
import com.inventory1.system.service.InventoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
public class InventoryController {

    private final ProductRepository productRepository;
    private final ServiceItemRepository serviceItemRepository;
    private final InventoryService inventoryService;

    // ==========================================
    //          PRODUCT ENDPOINTS (Stock)
    // ==========================================

    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productRepository.findAll());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PostMapping("/products/upload")
    public ResponseEntity<?> uploadCSVFile(@RequestParam("file") MultipartFile file) {
        if (file.isEmpty() || !file.getOriginalFilename().endsWith(".csv")) {
            return ResponseEntity.badRequest().body("Please upload a valid CSV file!");
        }

        try {
            List<Product> savedProducts = inventoryService.saveProductsFromCSV(file);
            return ResponseEntity.ok("Successfully uploaded " + savedProducts.size() + " products!");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Could not upload file: " + e.getMessage());
        }
    }

    // ==========================================
    // 🔥 EDIT a Product 🔥
    // ==========================================
    @PutMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Long id, @RequestBody Product updatedProduct) {
        return productRepository.findById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            existingProduct.setDescription(updatedProduct.getDescription());

            productRepository.save(existingProduct);
            return ResponseEntity.ok(existingProduct);
        }).orElse(ResponseEntity.notFound().build());
    }

    // ==========================================
    // 🔥 DELETE a Product 🔥
    // ==========================================
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> deleteProduct(@PathVariable Long id) {
        try {
            if (!productRepository.existsById(id)) {
                return ResponseEntity.notFound().build();
            }
            productRepository.deleteById(id);
            return ResponseEntity.ok().body("Product deleted successfully.");
        } catch (Exception e) {
            // If the database blocks the deletion (e.g., because it's attached to a past sale)
            return ResponseEntity.badRequest().body("Cannot delete this product because it is linked to past sales records.");
        }
    }

    // ==========================================
    //          SERVICE ENDPOINTS (Repairs)
    // ==========================================

    @GetMapping("/services")
    public ResponseEntity<List<ServiceItem>> getAllServices() {
        return ResponseEntity.ok(serviceItemRepository.findAll());
    }

    @PostMapping("/services")
    public ResponseEntity<ServiceItem> addService(@RequestBody ServiceItem serviceItem) {
        ServiceItem savedService = serviceItemRepository.save(serviceItem);
        return ResponseEntity.ok(savedService);
    }
}