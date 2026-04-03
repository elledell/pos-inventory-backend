package com.inventory1.system.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sales")
@Data
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String paymentMethod; // Cash, M-Pesa

    @Column(nullable = false)
    private Double totalAmount;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime saleDate;

    // ==========================================
    // 🔥 ADD THIS NEW LIST 🔥
    // This tells MySQL: "One Sale can have Many SaleItems attached to it."
    // cascade = CascadeType.ALL means if we save the Sale, it automatically saves the items too!
    // ==========================================
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL)
    private List<SaleItem> items = new ArrayList<>();
}