package com.inventory1.system.dto;

import lombok.Data;

@Data // Lombok automatically creates the getters/setters
public class ProductSaleDTO {
    private Long productId;
    private Integer quantitySold;
    private Double sellingPrice;


}