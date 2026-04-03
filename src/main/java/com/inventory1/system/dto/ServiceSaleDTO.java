package com.inventory1.system.dto;

import lombok.Data;

@Data
public class ServiceSaleDTO {
    private Long serviceId;
    private Double customPrice;
    private String description; // e.g., "Customer provided their own screen"
}