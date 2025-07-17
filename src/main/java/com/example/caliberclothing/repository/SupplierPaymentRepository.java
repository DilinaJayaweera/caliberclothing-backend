package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.SupplierPayment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierPaymentRepository extends JpaRepository<SupplierPayment, Integer> {
//    List<SupplierPayment> findByPaymentStatusId(Integer statusId);
//    List<SupplierPayment> findByPaymentMethodId(Integer methodId);
    Optional<SupplierPayment> findByReferenceNo(String referenceNo);
    List<SupplierPayment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
    Optional<SupplierPayment> findByPurchaseDetailsId(Integer purchaseDetailsId);
}
