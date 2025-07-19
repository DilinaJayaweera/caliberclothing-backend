package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.SupplierDetails;
import com.example.caliberclothing.repository.SupplierDetailsRepository;
import com.example.caliberclothing.service.SupplierDetailsService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class SupplierDetailsServiceImpl implements SupplierDetailsService {

        @Autowired
        private SupplierDetailsRepository supplierRepository;

        public List<SupplierDetails> getAllActiveSuppliers() {
            return supplierRepository.findByIsActiveTrue();
        }

        public Optional<SupplierDetails> getSupplierById(Integer id) {
            return supplierRepository.findByIdAndIsActiveTrue(id);
        }

        public Optional<SupplierDetails> getSupplierBySupplierNo(String supplierNo) {
            return supplierRepository.findBySupplierNoAndIsActiveTrue(supplierNo);
        }

        public List<SupplierDetails> searchSuppliersByName(String name) {
            return supplierRepository.findBySupplierNameContainingIgnoreCaseAndIsActiveTrue(name);
        }

        public List<SupplierDetails> getSuppliersByCountry(String country) {
            return supplierRepository.findByCountryAndIsActiveTrue(country);
        }

        public SupplierDetails saveSupplier(SupplierDetails supplier) {
            return supplierRepository.save(supplier);
        }

//        public SupplierDetails updateSupplier(SupplierDetails supplier) {
//            supplier.setUpdatedTimestamp(LocalDateTime.now());
//            return supplierRepository.save(supplier);
//        }

    public SupplierDetails updateSupplier(SupplierDetails supplier) {
        // Get existing supplier to preserve createdTimestamp
        SupplierDetails existingSupplier = supplierRepository.findById(supplier.getId())
                .orElseThrow(() -> new EntityNotFoundException("Supplier not found"));

        // Preserve original timestamps
        supplier.setCreatedTimestamp(existingSupplier.getCreatedTimestamp());
        supplier.setUpdatedTimestamp(LocalDateTime.now());

        return supplierRepository.save(supplier);
    }

        public void softDeleteSupplier(Integer id) {
            Optional<SupplierDetails> supplier = supplierRepository.findById(id);
            if (supplier.isPresent()) {
                SupplierDetails s = supplier.get();
                s.setIsActive(false);
                s.setDeletedTimestamp(LocalDateTime.now());
                supplierRepository.save(s);
            }
        }

    @Override
    public void deleteSupplier(Integer id) {
        supplierRepository.deleteById(id);
    }


}
