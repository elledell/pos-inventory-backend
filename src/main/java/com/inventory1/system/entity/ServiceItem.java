package com.inventory1.system.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "services_rendered")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ServiceItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name; // e.g., Phone Screen Repair

    @Column(nullable = false)
    private Double price;

    @Column(length = 255)
    private String description;
}