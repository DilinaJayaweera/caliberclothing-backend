package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.SupplierPaymentDTO;
import com.example.caliberclothing.entity.Inventory;
import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.entity.SupplierDetails;
import com.example.caliberclothing.service.InventoryService;
import com.example.caliberclothing.service.ProductService;
import com.example.caliberclothing.service.SupplierDetailsService;
import com.example.caliberclothing.service.SupplierPaymentService;
import com.example.caliberclothing.service.impl.CustomUserDetails;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/merchandise-manager")
@PreAuthorize("hasRole('MERCHANDISE_MANAGER')")
public class MerchandiseManagerController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private ProductService productService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getMerchandiseManagerDashboard(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Merchandise Manager Dashboard");
        response.put("username", userDetails.getUsername());
        response.put("role", userDetails.getRole());
        response.put("dashboardType", "merchandise_manager");
        response.put("permissions", new String[]{
                "inventory_management", "supplier_management", "supplier_payments",
                "product_reordering", "low_stock_notifications"
        });

        return ResponseEntity.ok(response);
    }

    // Low Stock Notifications
    @GetMapping("/notifications/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockNotifications() {
        List<Inventory> lowStockItems = inventoryService.getLowStockItems();
        return ResponseEntity.ok(lowStockItems);
    }

    @PostMapping("/notifications/mark-reordered/{productId}")
    public ResponseEntity<Map<String, Object>> markAsReordered(@PathVariable Integer productId) {
        try {
            // Update product status to reordered - you might need to add a status field to Product entity
            // For now, just return success
            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product marked as reordered");
            response.put("productId", productId);
            response.put("timestamp", LocalDateTime.now());

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Inventory Management
    @GetMapping("/inventory")
    public ResponseEntity<List<Inventory>> getAllInventory() {
        List<Inventory> inventories = inventoryService.getAllInventories();
        return ResponseEntity.ok(inventories);
    }

    @PutMapping("/inventory/{id}/quantity")
    public ResponseEntity<Inventory> updateInventoryQuantity(@PathVariable Integer id, @RequestParam Integer quantity) {
        Inventory updatedInventory = inventoryService.updateQuantity(id, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    @PutMapping("/inventory/{id}/reorder-level")
    public ResponseEntity<Inventory> updateReorderLevel(@PathVariable Integer id, @RequestParam Integer reorderLevel) {
        Inventory updatedInventory = inventoryService.updateReorderLevel(id, reorderLevel);
        return ResponseEntity.ok(updatedInventory);
    }

    @PatchMapping("/inventory/add-stock/{productId}")
    public ResponseEntity<Inventory> addStock(@PathVariable Integer productId, @RequestParam Integer quantity) {
        Inventory updatedInventory = inventoryService.addStock(productId, quantity);
        return ResponseEntity.ok(updatedInventory);
    }

    // Product Management (Update quantities when delivered)
    @PutMapping("/products/{id}/delivered")
    public ResponseEntity<Map<String, Object>> updateProductOnDelivery(
            @PathVariable Integer id,
            @RequestParam Integer deliveredQuantity) {
        try {
            // Add delivered quantity to inventory
            inventoryService.addStock(id, deliveredQuantity);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Product quantity updated on delivery");
            response.put("productId", id);
            response.put("deliveredQuantity", deliveredQuantity);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // Supplier Management
    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierDetails>> getAllSuppliers() {
        List<SupplierDetails> suppliers = supplierDetailsService.getAllActiveSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping("/suppliers")
    public ResponseEntity<SupplierDetails> createSupplier(@Valid @RequestBody SupplierDetails supplier) {
        SupplierDetails savedSupplier = supplierDetailsService.saveSupplier(supplier);
        return ResponseEntity.ok(savedSupplier);
    }

    @PutMapping("/suppliers/{id}")
    public ResponseEntity<SupplierDetails> updateSupplier(@PathVariable Integer id, @Valid @RequestBody SupplierDetails supplier) {
        supplier.setId(id);
        SupplierDetails updatedSupplier = supplierDetailsService.updateSupplier(supplier);
        return ResponseEntity.ok(updatedSupplier);
    }

    @GetMapping("/suppliers/search")
    public ResponseEntity<List<SupplierDetails>> searchSuppliers(@RequestParam String name) {
        List<SupplierDetails> suppliers = supplierDetailsService.searchSuppliersByName(name);
        return ResponseEntity.ok(suppliers);
    }

    // Supplier Payment Management
    @GetMapping("/supplier-payments")
    public ResponseEntity<List<SupplierPaymentDTO>> getAllSupplierPayments() {
        List<SupplierPaymentDTO> payments = supplierPaymentService.getAllSupplierPayments();
        return ResponseEntity.ok(payments);
    }

    @PostMapping("/supplier-payments")
    public ResponseEntity<SupplierPaymentDTO> createSupplierPayment(@Valid @RequestBody SupplierPaymentDTO paymentDTO) {
        SupplierPaymentDTO createdPayment = supplierPaymentService.createSupplierPayment(paymentDTO);
        return ResponseEntity.ok(createdPayment);
    }

    @PutMapping("/supplier-payments/{id}")
    public ResponseEntity<SupplierPaymentDTO> updateSupplierPayment(@PathVariable Integer id, @Valid @RequestBody SupplierPaymentDTO paymentDTO) {
        SupplierPaymentDTO updatedPayment = supplierPaymentService.updateSupplierPayment(id, paymentDTO);
        return ResponseEntity.ok(updatedPayment);
    }

    @GetMapping("/supplier-payments/date-range")
    public ResponseEntity<List<SupplierPaymentDTO>> getSupplierPaymentsByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<SupplierPaymentDTO> payments = supplierPaymentService.getSupplierPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payments);
    }
}