package com.inventory1.system.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "settings")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class StoreSettings {
    @Id
    private Long id = 1L; // We only ever need 1 row of settings

    private String adminPin = "0000"; // The default PIN out of the box
}