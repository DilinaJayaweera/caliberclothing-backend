package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.Inventory;
import com.example.caliberclothing.repository.InventoryRepository;
import com.example.caliberclothing.service.InventoryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class InventoryServiceImpl implements InventoryService {

    private final InventoryRepository inventoryRepository;

    public List<Inventory> getAllInventories() {
        log.info("Fetching all inventory records");
        return inventoryRepository.findAll();
    }

    public Optional<Inventory> getInventoryById(Integer id) {
        log.info("Fetching inventory with id: {}", id);
        return inventoryRepository.findById(id);
    }

    public Optional<Inventory> getInventoryByProductId(Integer productId) {
        log.info("Fetching inventory for product id: {}", productId);
        return inventoryRepository.findByProductId(productId);
    }

    public Inventory createInventory(Inventory inventory) {
        log.info("Creating new inventory record for product: {}", inventory.getProduct().getId());

        if (inventoryRepository.existsByProductId(inventory.getProduct().getId())) {
            throw new IllegalArgumentException("Inventory already exists for this product");
        }

        return inventoryRepository.save(inventory);
    }

    public Inventory updateInventory(Integer id, Inventory inventoryDetails) {
        log.info("Updating inventory with id: {}", id);

        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setReorderLevel(inventoryDetails.getReorderLevel());
                    inventory.setTotalQuantityPurchasing(inventoryDetails.getTotalQuantityPurchasing());

                    if (inventoryDetails.getProduct() != null) {
                        // Check if another inventory exists for the new product
                        Optional<Inventory> existingInventory = inventoryRepository.findByProductId(inventoryDetails.getProduct().getId());
                        if (existingInventory.isPresent() && !existingInventory.get().getId().equals(id)) {
                            throw new IllegalArgumentException("Inventory already exists for this product");
                        }
                        inventory.setProduct(inventoryDetails.getProduct());
                    }

                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found with id: " + id));
    }

    public void deleteInventory(Integer id) {
        log.info("Deleting inventory with id: {}", id);

        if (!inventoryRepository.existsById(id)) {
            throw new IllegalArgumentException("Inventory not found with id: " + id);
        }

        inventoryRepository.deleteById(id);
    }

    public List<Inventory> getLowStockItems() {
        log.info("Fetching low stock items");
        return inventoryRepository.findLowStockItems();
    }

    public List<Inventory> getItemsByQuantityLessThan(Integer quantity) {
        log.info("Fetching items with quantity less than: {}", quantity);
        return inventoryRepository.findByQuantityLessThan(quantity);
    }

    public List<Inventory> getItemsByReorderLevel(Integer reorderLevel) {
        log.info("Fetching items with reorder level: {}", reorderLevel);
        return inventoryRepository.findByReorderLevel(reorderLevel);
    }

    public Inventory updateQuantity(Integer id, Integer newQuantity) {
        log.info("Updating quantity for inventory id: {} to: {}", id, newQuantity);

        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setTotalQuantityPurchasing(newQuantity);
                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found with id: " + id));
    }

    public Inventory updateReorderLevel(Integer id, Integer newReorderLevel) {
        log.info("Updating reorder level for inventory id: {} to: {}", id, newReorderLevel);

        return inventoryRepository.findById(id)
                .map(inventory -> {
                    inventory.setReorderLevel(newReorderLevel);
                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found with id: " + id));
    }

    public boolean checkStockAvailability(Integer productId, Integer requiredQuantity) {
        log.info("Checking stock availability for product: {} with required quantity: {}", productId, requiredQuantity);

        return inventoryRepository.findByProductId(productId)
                .map(inventory -> inventory.getTotalQuantityPurchasing() >= requiredQuantity)
                .orElse(false);
    }

    public Inventory reduceStock(Integer productId, Integer quantity) {
        log.info("Reducing stock for product: {} by quantity: {}", productId, quantity);

        return inventoryRepository.findByProductId(productId)
                .map(inventory -> {
                    int currentQuantity = inventory.getTotalQuantityPurchasing();
                    if (currentQuantity < quantity) {
                        throw new IllegalArgumentException("Insufficient stock. Available: " + currentQuantity + ", Required: " + quantity);
                    }

                    inventory.setTotalQuantityPurchasing(currentQuantity - quantity);
                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product: " + productId));
    }

    public Inventory addStock(Integer productId, Integer quantity) {
        log.info("Adding stock for product: {} by quantity: {}", productId, quantity);

        return inventoryRepository.findByProductId(productId)
                .map(inventory -> {
                    int currentQuantity = inventory.getTotalQuantityPurchasing();
                    inventory.setTotalQuantityPurchasing(currentQuantity + quantity);
                    return inventoryRepository.save(inventory);
                })
                .orElseThrow(() -> new IllegalArgumentException("Inventory not found for product: " + productId));
    }
}

