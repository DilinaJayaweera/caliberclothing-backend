package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Integer> {

    Optional<Inventory> findByProductId(Integer productId);

    @Query("SELECT i FROM Inventory i WHERE i.totalQuantityPurchasing <= i.reorderLevel")
    List<Inventory> findLowStockItems();

    @Query("SELECT i FROM Inventory i WHERE i.totalQuantityPurchasing < :quantity")
    List<Inventory> findByQuantityLessThan(@Param("quantity") Integer quantity);

    @Query("SELECT i FROM Inventory i WHERE i.reorderLevel = :reorderLevel")
    List<Inventory> findByReorderLevel(@Param("reorderLevel") Integer reorderLevel);

    boolean existsByProductId(Integer productId);
}
