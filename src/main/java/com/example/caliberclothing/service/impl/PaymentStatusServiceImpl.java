package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.PaymentStatusDTO;
import com.example.caliberclothing.entity.PaymentStatus;
import com.example.caliberclothing.repository.PaymentStatusRepository;
import com.example.caliberclothing.service.PaymentStatusService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
public class PaymentStatusServiceImpl implements PaymentStatusService {

    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentStatusServiceImpl(PaymentStatusRepository paymentStatusRepository) {
        this.paymentStatusRepository = paymentStatusRepository;
    }

    public List<PaymentStatusDTO> getAllPaymentStatuses() {
        List<PaymentStatus> statuses = paymentStatusRepository.findAll();
        return statuses.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public PaymentStatusDTO getPaymentStatusById(Integer id) {
        PaymentStatus status = paymentStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment status not found"));
        return convertToDto(status);
    }

    private PaymentStatusDTO convertToDto(PaymentStatus status) {
        return PaymentStatusDTO.builder()
                .id(status.getId())
                .value(status.getValue())
                .build();
    }
}
