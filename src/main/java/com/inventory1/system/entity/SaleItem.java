package com.inventory1.system.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "sale_items")
@Data
public class SaleItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String itemName; // e.g. "Samsung Type-C Charger" or "Screen Replacement"

    @Column(nullable = false)
    private String itemType; // We will save either "PRODUCT" or "SERVICE" here

    @Column(nullable = false)
    private Integer quantity;

    @Column(nullable = false)
    private Double unitPrice; // The price per item

    @Column(nullable = false)
    private Double subTotal; // quantity * unitPrice

    // This is the magic link! It attaches this specific item to the main Sale receipt.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id", nullable = false)
    @JsonIgnore // Prevents an infinite loop when sending data to React
    private Sale sale;
}