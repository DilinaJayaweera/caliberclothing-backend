package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.PurchaseDetails;
import com.example.caliberclothing.service.PurchaseDetailsService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/purchase-details")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class PurchaseDetailsController {

    private final PurchaseDetailsService purchaseDetailsService;

    @PostMapping
    public ResponseEntity<PurchaseDetails> createPurchaseDetails(@Valid @RequestBody PurchaseDetails purchaseDetails) {
        log.info("REST request to create purchase details");
        PurchaseDetails created = purchaseDetailsService.createPurchaseDetails(purchaseDetails);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PurchaseDetails> updatePurchaseDetails(
            @PathVariable Integer id,
            @Valid @RequestBody PurchaseDetails purchaseDetails) {
        log.info("REST request to update purchase details with ID: {}", id);
        PurchaseDetails updated = purchaseDetailsService.updatePurchaseDetails(id, purchaseDetails);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PurchaseDetails> getPurchaseDetailsById(@PathVariable Integer id) {
        log.info("REST request to get purchase details with ID: {}", id);
        return purchaseDetailsService.getPurchaseDetailsById(id)
                .map(purchaseDetails -> ResponseEntity.ok(purchaseDetails))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/purchase-number/{purchaseNumber}")
    public ResponseEntity<PurchaseDetails> getPurchaseDetailsByPurchaseNumber(@PathVariable String purchaseNumber) {
        log.info("REST request to get purchase details with purchase number: {}", purchaseNumber);
        return purchaseDetailsService.getPurchaseDetailsByPurchaseNumber(purchaseNumber)
                .map(purchaseDetails -> ResponseEntity.ok(purchaseDetails))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<PurchaseDetails>> getAllPurchaseDetails() {
        log.info("REST request to get all purchase details");
        List<PurchaseDetails> purchaseDetailsList = purchaseDetailsService.getAllPurchaseDetails();
        return ResponseEntity.ok(purchaseDetailsList);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<PurchaseDetails>> getPurchaseDetailsWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDirection) {

        log.info("REST request to get purchase details with pagination");

        Sort sort = sortDirection.equalsIgnoreCase("desc") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<PurchaseDetails> purchaseDetailsPage = purchaseDetailsService.getPurchaseDetailsWithPagination(pageable);

        return ResponseEntity.ok(purchaseDetailsPage);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePurchaseDetails(@PathVariable Integer id) {
        log.info("REST request to delete purchase details with ID: {}", id);
        purchaseDetailsService.deletePurchaseDetails(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PurchaseDetails>> getPurchaseDetailsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {

        log.info("REST request to get purchase details by date range: {} to {}", startDate, endDate);
        List<PurchaseDetails> purchaseDetailsList = purchaseDetailsService.getPurchaseDetailsByDateRange(startDate, endDate);
        return ResponseEntity.ok(purchaseDetailsList);
    }

    @GetMapping("/delivery-date-range")
    public ResponseEntity<List<PurchaseDetails>> getPurchaseDetailsByDeliveryDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {

        log.info("REST request to get purchase details by delivery date range: {} to {}", startDate, endDate);
        List<PurchaseDetails> purchaseDetailsList = purchaseDetailsService.getPurchaseDetailsByDeliveryDateRange(startDate, endDate);
        return ResponseEntity.ok(purchaseDetailsList);
    }

    @GetMapping("/supplier/{supplierId}")
    public ResponseEntity<List<PurchaseDetails>> getPurchaseDetailsBySupplier(@PathVariable Integer supplierId) {
        log.info("REST request to get purchase details by supplier ID: {}", supplierId);
        List<PurchaseDetails> purchaseDetailsList = purchaseDetailsService.getPurchaseDetailsBySupplier(supplierId);
        return ResponseEntity.ok(purchaseDetailsList);
    }

    @GetMapping("/grn/{grnId}")
    public ResponseEntity<List<PurchaseDetails>> getPurchaseDetailsByGrn(@PathVariable Integer grnId) {
        log.info("REST request to get purchase details by GRN ID: {}", grnId);
        List<PurchaseDetails> purchaseDetailsList = purchaseDetailsService.getPurchaseDetailsByGrn(grnId);
        return ResponseEntity.ok(purchaseDetailsList);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PurchaseDetails>> searchByProduct(@RequestParam String product) {
        log.info("REST request to search purchase details by product: {}", product);
        List<PurchaseDetails> purchaseDetailsList = purchaseDetailsService.searchByProduct(product);
        return ResponseEntity.ok(purchaseDetailsList);
    }

    @GetMapping("/overdue")
    public ResponseEntity<List<PurchaseDetails>> getOverdueDeliveries() {
        log.info("REST request to get overdue deliveries");
        List<PurchaseDetails> overdueDeliveries = purchaseDetailsService.getOverdueDeliveries();
        return ResponseEntity.ok(overdueDeliveries);
    }

    @GetMapping("/exists/{purchaseNumber}")
    public ResponseEntity<Boolean> existsByPurchaseNumber(@PathVariable String purchaseNumber) {
        log.info("REST request to check if purchase number exists: {}", purchaseNumber);
        boolean exists = purchaseDetailsService.existsByPurchaseNumber(purchaseNumber);
        return ResponseEntity.ok(exists);
    }
}
