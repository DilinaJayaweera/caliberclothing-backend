package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.Inventory;

import java.util.List;
import java.util.Optional;

public interface InventoryService {

    public List<Inventory> getAllInventories();

    public Optional<Inventory> getInventoryById(Integer id);

    public Optional<Inventory> getInventoryByProductId(Integer productId);

    public Inventory createInventory(Inventory inventory);

    public Inventory updateInventory(Integer id, Inventory inventoryDetails);

    public void deleteInventory(Integer id);

    public List<Inventory> getLowStockItems();

    public List<Inventory> getItemsByQuantityLessThan(Integer quantity);

    public List<Inventory> getItemsByReorderLevel(Integer reorderLevel);

    public Inventory updateQuantity(Integer id, Integer newQuantity);

    public Inventory updateReorderLevel(Integer id, Integer newReorderLevel);

    public boolean checkStockAvailability(Integer productId, Integer requiredQuantity);

    public Inventory reduceStock(Integer productId, Integer quantity);

    public Inventory addStock(Integer productId, Integer quantity);

}
