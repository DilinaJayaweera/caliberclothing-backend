package com.example.caliberclothing.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@PreAuthorize("hasRole('MERCHANDISE_MANAGER')")
public class NotificationController {

    @GetMapping("/low-stock")
    public ResponseEntity<List<Map<String, Object>>> getLowStockNotifications() {
        // Get products with quantity <= reorder level
        return ResponseEntity.ok(List.of());
    }

    @PostMapping("/mark-reordered/{productId}")
    public ResponseEntity<Map<String, Object>> markAsReordered(@PathVariable Integer productId) {
        // Update product status to reordered
        return ResponseEntity.ok(Map.of("success", true));
    }
}
