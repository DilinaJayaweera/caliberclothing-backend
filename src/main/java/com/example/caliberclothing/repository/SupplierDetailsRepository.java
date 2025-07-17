package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.SupplierDetails;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SupplierDetailsRepository extends JpaRepository<SupplierDetails, Integer> {

    List<SupplierDetails> findByIsActiveTrue();

    Optional<SupplierDetails> findBySupplierNoAndIsActiveTrue(String supplierNo);

    List<SupplierDetails> findBySupplierNameContainingIgnoreCaseAndIsActiveTrue(String supplierName);

    List<SupplierDetails> findByCountryAndIsActiveTrue(String country);

    Optional<SupplierDetails> findByIdAndIsActiveTrue(Integer id);
}
