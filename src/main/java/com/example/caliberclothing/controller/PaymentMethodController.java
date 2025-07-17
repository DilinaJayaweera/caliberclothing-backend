package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.PaymentMethodDTO;
import com.example.caliberclothing.service.PaymentMethodService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/payment-methods")
//@CrossOrigin(origins = "*")
public class PaymentMethodController {

    private final PaymentMethodService paymentMethodService;

    public PaymentMethodController(PaymentMethodService paymentMethodService) {
        this.paymentMethodService = paymentMethodService;
    }

    @GetMapping
    public ResponseEntity<List<PaymentMethodDTO>> getAllPaymentMethods() {
        List<PaymentMethodDTO> methods = paymentMethodService.getAllPaymentMethods();
        return ResponseEntity.ok(methods);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PaymentMethodDTO> getPaymentMethodById(@PathVariable Integer id) {
        try {
            PaymentMethodDTO method = paymentMethodService.getPaymentMethodById(id);
            return ResponseEntity.ok(method);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}
