package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.SupplierDetails;
import com.example.caliberclothing.service.SupplierDetailsService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/suppliers")
//@CrossOrigin(origins = "*")
public class SupplierDetailsController {

    @Autowired
    private SupplierDetailsService supplierService;

    @GetMapping
    public ResponseEntity<List<SupplierDetails>> getAllSuppliers() {
        List<SupplierDetails> suppliers = supplierService.getAllActiveSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierDetails> getSupplierById(@PathVariable Integer id) {
        Optional<SupplierDetails> supplier = supplierService.getSupplierById(id);
        return supplier.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-supplier-no/{supplierNo}")
    public ResponseEntity<SupplierDetails> getSupplierBySupplierNo(@PathVariable String supplierNo) {
        Optional<SupplierDetails> supplier = supplierService.getSupplierBySupplierNo(supplierNo);
        return supplier.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/search")
    public ResponseEntity<List<SupplierDetails>> searchSuppliers(@RequestParam String name) {
        List<SupplierDetails> suppliers = supplierService.searchSuppliersByName(name);
        return ResponseEntity.ok(suppliers);
    }

    @GetMapping("/country/{country}")
    public ResponseEntity<List<SupplierDetails>> getSuppliersByCountry(@PathVariable String country) {
        List<SupplierDetails> suppliers = supplierService.getSuppliersByCountry(country);
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping
    public ResponseEntity<SupplierDetails> createSupplier(@Valid @RequestBody SupplierDetails supplier) {
        SupplierDetails savedSupplier = supplierService.saveSupplier(supplier);
        return ResponseEntity.ok(savedSupplier);
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierDetails> updateSupplier(@PathVariable Integer id, @Valid @RequestBody SupplierDetails supplier) {
        Optional<SupplierDetails> existingSupplier = supplierService.getSupplierById(id);
        if (existingSupplier.isPresent()) {
            supplier.setId(id);
            SupplierDetails updatedSupplier = supplierService.updateSupplier(supplier);
            return ResponseEntity.ok(updatedSupplier);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Integer id) {
        supplierService.softDeleteSupplier(id);
        return ResponseEntity.noContent().build();
    }
}

