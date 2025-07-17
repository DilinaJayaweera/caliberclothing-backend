package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.PaymentDTO;
import com.example.caliberclothing.service.PaymentService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/payments")
//@CrossOrigin(origins = "*")
public class PaymentController {

    private final PaymentService paymentService;

    public PaymentController(PaymentService paymentService) {
        this.paymentService = paymentService;
    }

    @PostMapping
    public ResponseEntity<PaymentDTO> createPayment(@Valid @RequestBody PaymentDTO requestDto) {
        try {
            PaymentDTO response = paymentService.createPayment(requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<PaymentDTO> updatePayment(@PathVariable Integer id,
                                                            @Valid @RequestBody PaymentDTO requestDto) {
        try {
            PaymentDTO response = paymentService.updatePayment(id, requestDto);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentDTO> getPaymentById(@PathVariable Integer id) {
        try {
            PaymentDTO response = paymentService.getPaymentById(id);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<PaymentDTO>> getAllPayments() {
        List<PaymentDTO> payments = paymentService.getAllPayments();
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByStatus(@PathVariable Integer statusId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByStatus(statusId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/method/{methodId}")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByMethod(@PathVariable Integer methodId) {
        List<PaymentDTO> payments = paymentService.getPaymentsByMethod(methodId);
        return ResponseEntity.ok(payments);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<PaymentDTO>> getPaymentsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<PaymentDTO> payments = paymentService.getPaymentsByDateRange(startDate, endDate);
        return ResponseEntity.ok(payments);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePayment(@PathVariable Integer id) {
        try {
            paymentService.deletePayment(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

