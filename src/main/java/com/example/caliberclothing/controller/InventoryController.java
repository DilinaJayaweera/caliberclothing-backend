package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.Inventory;
import com.example.caliberclothing.service.InventoryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventory")
@RequiredArgsConstructor
@Slf4j
//@CrossOrigin(origins = "*")
public class InventoryController {

    private final InventoryService inventoryService;

    @GetMapping
    public ResponseEntity<List<Inventory>> getAllInventories() {
        try {
            List<Inventory> inventories = inventoryService.getAllInventories();
            return ResponseEntity.ok(inventories);
        } catch (Exception e) {
            log.error("Error fetching all inventories: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable Integer id) {
        try {
            return inventoryService.getInventoryById(id)
                    .map(inventory -> ResponseEntity.ok(inventory))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching inventory with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<Inventory> getInventoryByProductId(@PathVariable Integer productId) {
        try {
            return inventoryService.getInventoryByProductId(productId)
                    .map(inventory -> ResponseEntity.ok(inventory))
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            log.error("Error fetching inventory for product {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    public ResponseEntity<Inventory> createInventory(@Valid @RequestBody Inventory inventory) {
        try {
            Inventory createdInventory = inventoryService.createInventory(inventory);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdInventory);
        } catch (IllegalArgumentException e) {
            log.error("Error creating inventory: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error creating inventory: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Inventory> updateInventory(@PathVariable Integer id,
                                                     @Valid @RequestBody Inventory inventoryDetails) {
        try {
            Inventory updatedInventory = inventoryService.updateInventory(id, inventoryDetails);
            return ResponseEntity.ok(updatedInventory);
        } catch (IllegalArgumentException e) {
            log.error("Error updating inventory with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error updating inventory with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInventory(@PathVariable Integer id) {
        try {
            inventoryService.deleteInventory(id);
            return ResponseEntity.noContent().build();
        } catch (IllegalArgumentException e) {
            log.error("Error deleting inventory with id {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error deleting inventory with id {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/low-stock")
    public ResponseEntity<List<Inventory>> getLowStockItems() {
        try {
            List<Inventory> lowStockItems = inventoryService.getLowStockItems();
            return ResponseEntity.ok(lowStockItems);
        } catch (Exception e) {
            log.error("Error fetching low stock items: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/quantity-less-than/{quantity}")
    public ResponseEntity<List<Inventory>> getItemsByQuantityLessThan(@PathVariable @Min(0) Integer quantity) {
        try {
            List<Inventory> items = inventoryService.getItemsByQuantityLessThan(quantity);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error fetching items with quantity less than {}: {}", quantity, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/reorder-level/{reorderLevel}")
    public ResponseEntity<List<Inventory>> getItemsByReorderLevel(@PathVariable @Min(0) Integer reorderLevel) {
        try {
            List<Inventory> items = inventoryService.getItemsByReorderLevel(reorderLevel);
            return ResponseEntity.ok(items);
        } catch (Exception e) {
            log.error("Error fetching items with reorder level {}: {}", reorderLevel, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/quantity")
    public ResponseEntity<Inventory> updateQuantity(@PathVariable Integer id,
                                                    @RequestParam @Min(0) Integer quantity) {
        try {
            Inventory updatedInventory = inventoryService.updateQuantity(id, quantity);
            return ResponseEntity.ok(updatedInventory);
        } catch (IllegalArgumentException e) {
            log.error("Error updating quantity for inventory {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error updating quantity for inventory {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/{id}/reorder-level")
    public ResponseEntity<Inventory> updateReorderLevel(@PathVariable Integer id,
                                                        @RequestParam @Min(0) Integer reorderLevel) {
        try {
            Inventory updatedInventory = inventoryService.updateReorderLevel(id, reorderLevel);
            return ResponseEntity.ok(updatedInventory);
        } catch (IllegalArgumentException e) {
            log.error("Error updating reorder level for inventory {}: {}", id, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error updating reorder level for inventory {}: {}", id, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/check-stock/{productId}")
    public ResponseEntity<Boolean> checkStockAvailability(@PathVariable Integer productId,
                                                          @RequestParam @Min(1) Integer requiredQuantity) {
        try {
            boolean available = inventoryService.checkStockAvailability(productId, requiredQuantity);
            return ResponseEntity.ok(available);
        } catch (Exception e) {
            log.error("Error checking stock availability for product {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/reduce-stock/{productId}")
    public ResponseEntity<Inventory> reduceStock(@PathVariable Integer productId,
                                                 @RequestParam @Min(1) Integer quantity) {
        try {
            Inventory updatedInventory = inventoryService.reduceStock(productId, quantity);
            return ResponseEntity.ok(updatedInventory);
        } catch (IllegalArgumentException e) {
            log.error("Error reducing stock for product {}: {}", productId, e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            log.error("Unexpected error reducing stock for product {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PatchMapping("/add-stock/{productId}")
    public ResponseEntity<Inventory> addStock(@PathVariable Integer productId,
                                              @RequestParam @Min(1) Integer quantity) {
        try {
            Inventory updatedInventory = inventoryService.addStock(productId, quantity);
            return ResponseEntity.ok(updatedInventory);
        } catch (IllegalArgumentException e) {
            log.error("Error adding stock for product {}: {}", productId, e.getMessage());
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            log.error("Unexpected error adding stock for product {}: {}", productId, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}

