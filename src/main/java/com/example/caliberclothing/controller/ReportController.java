package com.example.caliberclothing.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.Map;

@RestController
@RequestMapping("/api/reports")
@PreAuthorize("hasRole('CEO')")
public class ReportController {

    @GetMapping("/sales")
    public ResponseEntity<Map<String, Object>> getSalesReport(
            @RequestParam String period,
            @RequestParam(required = false) LocalDateTime startDate,
            @RequestParam(required = false) LocalDateTime endDate) {
        // Implement sales report logic
        return ResponseEntity.ok(Map.of("report", "sales_data"));
    }

    @GetMapping("/customer-growth")
    public ResponseEntity<Map<String, Object>> getCustomerGrowthReport(
            @RequestParam String period) {
        // Implement customer growth report logic
        return ResponseEntity.ok(Map.of("report", "growth_data"));
    }

    @GetMapping("/most-sold-products")
    public ResponseEntity<Map<String, Object>> getMostSoldProductsReport(
            @RequestParam String period,
            @RequestParam(defaultValue = "10") int limit) {
        // Implement most sold products report logic
        return ResponseEntity.ok(Map.of("report", "product_data"));
    }
}
