package com.inventory1.system.controller;

import com.inventory1.system.dto.SaleRequestDTO;
import com.inventory1.system.entity.Sale;
import com.inventory1.system.service.CheckoutService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/checkout")
@RequiredArgsConstructor
public class CheckoutController {

    private final CheckoutService checkoutService;

    @PostMapping
    public ResponseEntity<?> processCheckout(@RequestBody SaleRequestDTO request) {
        try {
            // We hand the JSON payload from React straight to our engine
            Sale completedSale = checkoutService.processCheckout(request);

            // If it works, send back a 200 OK status and the final receipt data
            return ResponseEntity.ok(completedSale);

        } catch (RuntimeException e) {
            // If the engine throws an error (like "Not enough stock"),
            // catch it and tell React it was a 400 Bad Request with the error message
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}