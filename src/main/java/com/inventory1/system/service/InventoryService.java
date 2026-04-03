package com.inventory1.system.service;

import com.inventory1.system.entity.Product;
import com.inventory1.system.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class InventoryService {

    private final ProductRepository productRepository;

    public List<Product> saveProductsFromCSV(MultipartFile file) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(file.getInputStream(), StandardCharsets.UTF_8));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.builder()
                             .setHeader() // Tells the parser the first row is titles (Name, Category, etc.)
                             .setSkipHeaderRecord(true)
                             .setIgnoreHeaderCase(true)
                             .setTrim(true)
                             .build())) {

            List<Product> productsToSave = new ArrayList<>();

            for (CSVRecord csvRecord : csvParser) {
                Product product = new Product();
                // These must match the column headers in his Excel/CSV file exactly
                product.setName(csvRecord.get("name"));
                product.setCategory(csvRecord.get("category"));
                product.setPrice(Double.parseDouble(csvRecord.get("price")));
                product.setStockQuantity(Integer.parseInt(csvRecord.get("stockQuantity")));

                // Description is optional, so we check if it exists in the CSV
                if (csvRecord.isMapped("description")) {
                    product.setDescription(csvRecord.get("description"));
                }

                productsToSave.add(product);
            }

            // Saves the entire list to the MySQL database in one fast batch!
            return productRepository.saveAll(productsToSave);

        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}