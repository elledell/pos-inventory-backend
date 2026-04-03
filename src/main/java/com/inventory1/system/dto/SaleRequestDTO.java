package com.inventory1.system.dto;

import lombok.Data;
import java.util.List;

@Data
public class SaleRequestDTO {
    private String paymentMethod; // e.g., "Cash", "M-Pesa"

    // A list of the products being bought
    private List<ProductSaleDTO> products;

    // A list of the services rendered
    private List<ServiceSaleDTO> services;
}