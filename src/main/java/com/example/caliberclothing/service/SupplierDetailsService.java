package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.SupplierDetails;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface SupplierDetailsService {

    public List<SupplierDetails> getAllActiveSuppliers();

    public Optional<SupplierDetails> getSupplierById(Integer id);

    public Optional<SupplierDetails> getSupplierBySupplierNo(String supplierNo);

    public List<SupplierDetails> searchSuppliersByName(String name);

    public List<SupplierDetails> getSuppliersByCountry(String country);

    public SupplierDetails saveSupplier(SupplierDetails supplier);

    public SupplierDetails updateSupplier(SupplierDetails supplier);

    public void softDeleteSupplier(Integer id);

    void deleteSupplier(Integer id);

}
