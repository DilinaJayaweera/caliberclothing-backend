package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.PurchaseDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface PurchaseDetailsService {

    PurchaseDetails createPurchaseDetails(PurchaseDetails purchaseDetails);

    PurchaseDetails updatePurchaseDetails(Integer id, PurchaseDetails purchaseDetails);

    Optional<PurchaseDetails> getPurchaseDetailsById(Integer id);

    Optional<PurchaseDetails> getPurchaseDetailsByPurchaseNumber(String purchaseNumber);

    List<PurchaseDetails> getAllPurchaseDetails();

    Page<PurchaseDetails> getPurchaseDetailsWithPagination(Pageable pageable);

    void deletePurchaseDetails(Integer id);

    List<PurchaseDetails> getPurchaseDetailsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<PurchaseDetails> getPurchaseDetailsByDeliveryDateRange(LocalDate startDate, LocalDate endDate);

    List<PurchaseDetails> getPurchaseDetailsBySupplier(Integer supplierId);

    List<PurchaseDetails> getPurchaseDetailsByGrn(Integer grnId);

    List<PurchaseDetails> searchByProduct(String product);

    List<PurchaseDetails> getOverdueDeliveries();

    boolean existsByPurchaseNumber(String purchaseNumber);
}
