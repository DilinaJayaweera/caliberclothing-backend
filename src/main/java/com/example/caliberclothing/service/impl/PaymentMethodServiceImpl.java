package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.PaymentMethodDTO;
import com.example.caliberclothing.entity.PaymentMethod;
import com.example.caliberclothing.repository.PaymentMethodRepository;
import com.example.caliberclothing.service.PaymentMethodService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PaymentMethodServiceImpl implements PaymentMethodService {

    private final PaymentMethodRepository paymentMethodRepository;

    public PaymentMethodServiceImpl(PaymentMethodRepository paymentMethodRepository) {
        this.paymentMethodRepository = paymentMethodRepository;
    }

    public List<PaymentMethodDTO> getAllPaymentMethods() {
        List<PaymentMethod> methods = paymentMethodRepository.findAll();
        return methods.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public PaymentMethodDTO getPaymentMethodById(Integer id) {
        PaymentMethod method = paymentMethodRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment method not found"));
        return convertToDTO(method);
    }

    private PaymentMethodDTO convertToDTO(PaymentMethod method) {
        return PaymentMethodDTO.builder()
                .id(method.getId())
                .value(method.getValue())
                .build();
    }
}

