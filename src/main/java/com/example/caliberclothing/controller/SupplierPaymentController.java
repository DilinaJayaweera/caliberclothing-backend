package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.SupplierPaymentDTO;
import com.example.caliberclothing.service.SupplierPaymentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/supplier-payments")
//@CrossOrigin(origins = "*")
public class SupplierPaymentController {

    private final SupplierPaymentService supplierPaymentService;

    public SupplierPaymentController(SupplierPaymentService supplierPaymentService) {
        this.supplierPaymentService = supplierPaymentService;
    }

    @PostMapping
    public ResponseEntity<SupplierPaymentDTO> createSupplierPayment(@Valid @RequestBody SupplierPaymentDTO requestDto) {
        try {
            SupplierPaymentDTO response = supplierPaymentService.createSupplierPayment(requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<SupplierPaymentDTO> updateSupplierPayment(@PathVariable Integer id,
                                                                            @Valid @RequestBody SupplierPaymentDTO requestDto) {
        try {
            SupplierPaymentDTO response = supplierPaymentService.updateSupplierPayment(id, requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<SupplierPaymentDTO> getSupplierPaymentById(@PathVariable Integer id) {
        try {
            SupplierPaymentDTO response = supplierPaymentService.getSupplierPaymentById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<SupplierPaymentDTO>> getAllSupplierPayments() {
        List<SupplierPaymentDTO> payments = supplierPaymentService.getAllSupplierPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<SupplierPaymentDTO>> getSupplierPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<SupplierPaymentDTO> payments = supplierPaymentService.getSupplierPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSupplierPayment(@PathVariable Integer id) {
        try {
            supplierPaymentService.deleteSupplierPayment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

