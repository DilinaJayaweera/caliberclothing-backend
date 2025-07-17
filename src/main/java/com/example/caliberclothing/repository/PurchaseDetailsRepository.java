package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.PurchaseDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PurchaseDetailsRepository extends JpaRepository<PurchaseDetails, Integer> {
    Optional<PurchaseDetails> findByPurchaseNumber(String purchaseNumber);

    List<PurchaseDetails> findByDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    List<PurchaseDetails> findByExpectedDeliveryDateBetween(LocalDate startDate, LocalDate endDate);

    List<PurchaseDetails> findBySupplierDetailsId(Integer supplierId);

    List<PurchaseDetails> findByGrnId(Integer grnId);

    @Query("SELECT p FROM PurchaseDetails p WHERE p.product LIKE %:product%")
    List<PurchaseDetails> findByProductContaining(@Param("product") String product);

    @Query("SELECT p FROM PurchaseDetails p WHERE p.expectedDeliveryDate < :date")
    List<PurchaseDetails> findOverdueDeliveries(@Param("date") LocalDate date);

    boolean existsByPurchaseNumber(String purchaseNumber);
}

