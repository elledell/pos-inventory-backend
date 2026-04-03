package com.inventory1.system.repository;

import com.inventory1.system.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // Spring Boot automatically writes the SQL for this just based on the method name!
    List<Product> findByCategory(String category);

    // We can add a custom search to help the React frontend's dropdown later
    List<Product> findByNameContainingIgnoreCase(String name);
}