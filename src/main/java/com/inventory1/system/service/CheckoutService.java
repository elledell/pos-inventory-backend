package com.inventory1.system.service;

import com.inventory1.system.dto.ProductSaleDTO;
import com.inventory1.system.dto.SaleRequestDTO;
import com.inventory1.system.dto.ServiceSaleDTO;
import com.inventory1.system.entity.Product;
import com.inventory1.system.entity.Sale;
import com.inventory1.system.entity.SaleItem;
import com.inventory1.system.entity.ServiceItem; // Assuming you have a ServiceItem entity!
import com.inventory1.system.repository.ProductRepository;
import com.inventory1.system.repository.SaleRepository;
import com.inventory1.system.repository.ServiceItemRepository; // And its repository
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CheckoutService {

    private final ProductRepository productRepository;
    private final SaleRepository saleRepository;
    private final ServiceItemRepository serviceItemRepository;

    @Transactional
    public Sale processCheckout(SaleRequestDTO request) {
        double totalAmount = 0.0;
        Sale finalSale = new Sale();
        finalSale.setPaymentMethod(request.getPaymentMethod());

        // 1. Process physical products
        if (request.getProducts() != null) {
            for (ProductSaleDTO itemReq : request.getProducts()) {
                Product product = productRepository.findById(itemReq.getProductId())
                        .orElseThrow(() -> new RuntimeException("Product not found!"));

                if (product.getStockQuantity() < itemReq.getQuantitySold()) {
                    throw new RuntimeException("Not enough stock!");
                }
                product.setStockQuantity(product.getStockQuantity() - itemReq.getQuantitySold());
                productRepository.save(product);

                // Create the Receipt Line for this Product
                SaleItem lineItem = new SaleItem();
                lineItem.setItemName(product.getName());
                lineItem.setItemType("PRODUCT");
                lineItem.setQuantity(itemReq.getQuantitySold());
                lineItem.setUnitPrice(itemReq.getSellingPrice());
                lineItem.setSubTotal(itemReq.getSellingPrice() * itemReq.getQuantitySold());

                lineItem.setSale(finalSale); // Attach to receipt
                finalSale.getItems().add(lineItem);

                totalAmount += lineItem.getSubTotal();
            }
        }

        // 2. Process repair services
        if (request.getServices() != null) {
            for (ServiceSaleDTO servReq : request.getServices()) {
                ServiceItem service = serviceItemRepository.findById(servReq.getServiceId())
                        .orElseThrow(() -> new RuntimeException("Service not found!"));

                // Create the Receipt Line for this Service
                SaleItem lineItem = new SaleItem();
                lineItem.setItemName(service.getName());
                lineItem.setItemType("SERVICE");
                lineItem.setQuantity(1); // Services usually have a quantity of 1
                lineItem.setUnitPrice(servReq.getCustomPrice());
                lineItem.setSubTotal(servReq.getCustomPrice());

                lineItem.setSale(finalSale); // Attach to receipt
                finalSale.getItems().add(lineItem);

                totalAmount += lineItem.getSubTotal();
            }
        }

        finalSale.setTotalAmount(totalAmount);

        // Because of cascade = CascadeType.ALL, saving the Sale automatically saves all the SaleItems too!
        return saleRepository.save(finalSale);
    }
}