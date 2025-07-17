package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.PaymentStatusDTO;
import com.example.caliberclothing.service.PaymentStatusService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-statuses")
//@CrossOrigin(origins = "*")
public class PaymentStatusController {

    private final PaymentStatusService paymentStatusService;

    public PaymentStatusController(PaymentStatusService paymentStatusService) {
        this.paymentStatusService = paymentStatusService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentStatusDTO>> getAllPaymentStatuses() {
        List<PaymentStatusDTO> statuses = paymentStatusService.getAllPaymentStatuses();
        return ResponseEntity.ok(statuses);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentStatusDTO> getPaymentStatusById(@PathVariable Integer id) {
        try {
            PaymentStatusDTO status = paymentStatusService.getPaymentStatusById(id);
            return ResponseEntity.ok(status);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}

