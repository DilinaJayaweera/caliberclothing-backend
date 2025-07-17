package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.PurchaseDetails;
import com.example.caliberclothing.exception.DuplicateResourceException;
import com.example.caliberclothing.exception.ResourceNotFoundException;
import com.example.caliberclothing.repository.PurchaseDetailsRepository;
import com.example.caliberclothing.service.PurchaseDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class PurchaseDetailsServiceImpl implements PurchaseDetailsService {

    private final PurchaseDetailsRepository purchaseDetailsRepository;

    @Override
    public PurchaseDetails createPurchaseDetails(PurchaseDetails purchaseDetails) {
        log.info("Creating purchase details with purchase number: {}", purchaseDetails.getPurchaseNumber());

        if (purchaseDetailsRepository.existsByPurchaseNumber(purchaseDetails.getPurchaseNumber())) {
            throw new DuplicateResourceException("Purchase number already exists: " + purchaseDetails.getPurchaseNumber());
        }

        PurchaseDetails savedPurchaseDetails = purchaseDetailsRepository.save(purchaseDetails);
        log.info("Successfully created purchase details with ID: {}", savedPurchaseDetails.getId());
        return savedPurchaseDetails;
    }

    @Override
    public PurchaseDetails updatePurchaseDetails(Integer id, PurchaseDetails purchaseDetails) {
        log.info("Updating purchase details with ID: {}", id);

        PurchaseDetails existingPurchaseDetails = purchaseDetailsRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Purchase details not found with ID: " + id));

        // Check if purchase number is being changed and if it already exists
        if (!existingPurchaseDetails.getPurchaseNumber().equals(purchaseDetails.getPurchaseNumber()) &&
                purchaseDetailsRepository.existsByPurchaseNumber(purchaseDetails.getPurchaseNumber())) {
            throw new DuplicateResourceException("Purchase number already exists: " + purchaseDetails.getPurchaseNumber());
        }

        existingPurchaseDetails.setPurchaseNumber(purchaseDetails.getPurchaseNumber());
        existingPurchaseDetails.setDate(purchaseDetails.getDate());
        existingPurchaseDetails.setProduct(purchaseDetails.getProduct());
        existingPurchaseDetails.setExpectedDeliveryDate(purchaseDetails.getExpectedDeliveryDate());
        existingPurchaseDetails.setGrn(purchaseDetails.getGrn());
        existingPurchaseDetails.setSupplierDetails(purchaseDetails.getSupplierDetails());

        PurchaseDetails updatedPurchaseDetails = purchaseDetailsRepository.save(existingPurchaseDetails);
        log.info("Successfully updated purchase details with ID: {}", id);
        return updatedPurchaseDetails;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseDetails> getPurchaseDetailsById(Integer id) {
        log.info("Fetching purchase details with ID: {}", id);
        return purchaseDetailsRepository.findById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<PurchaseDetails> getPurchaseDetailsByPurchaseNumber(String purchaseNumber) {
        log.info("Fetching purchase details with purchase number: {}", purchaseNumber);
        return purchaseDetailsRepository.findByPurchaseNumber(purchaseNumber);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDetails> getAllPurchaseDetails() {
        log.info("Fetching all purchase details");
        return purchaseDetailsRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Page<PurchaseDetails> getPurchaseDetailsWithPagination(Pageable pageable) {
        log.info("Fetching purchase details with pagination: page {}, size {}",
                pageable.getPageNumber(), pageable.getPageSize());
        return purchaseDetailsRepository.findAll(pageable);
    }

    @Override
    public void deletePurchaseDetails(Integer id) {
        log.info("Deleting purchase details with ID: {}", id);

        if (!purchaseDetailsRepository.existsById(id)) {
            throw new ResourceNotFoundException("Purchase details not found with ID: " + id);
        }

        purchaseDetailsRepository.deleteById(id);
        log.info("Successfully deleted purchase details with ID: {}", id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDetails> getPurchaseDetailsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        log.info("Fetching purchase details between dates: {} and {}", startDate, endDate);
        return purchaseDetailsRepository.findByDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDetails> getPurchaseDetailsByDeliveryDateRange(LocalDate startDate, LocalDate endDate) {
        log.info("Fetching purchase details with delivery dates between: {} and {}", startDate, endDate);
        return purchaseDetailsRepository.findByExpectedDeliveryDateBetween(startDate, endDate);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDetails> getPurchaseDetailsBySupplier(Integer supplierId) {
        log.info("Fetching purchase details for supplier ID: {}", supplierId);
        return purchaseDetailsRepository.findBySupplierDetailsId(supplierId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDetails> getPurchaseDetailsByGrn(Integer grnId) {
        log.info("Fetching purchase details for GRN ID: {}", grnId);
        return purchaseDetailsRepository.findByGrnId(grnId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDetails> searchByProduct(String product) {
        log.info("Searching purchase details by product: {}", product);
        return purchaseDetailsRepository.findByProductContaining(product);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PurchaseDetails> getOverdueDeliveries() {
        log.info("Fetching overdue deliveries");
        return purchaseDetailsRepository.findOverdueDeliveries(LocalDate.now());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByPurchaseNumber(String purchaseNumber) {
        return purchaseDetailsRepository.existsByPurchaseNumber(purchaseNumber);
    }
}
