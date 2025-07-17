package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.PaymentDTO;
import com.example.caliberclothing.dto.PaymentMethodDTO;
import com.example.caliberclothing.dto.PaymentStatusDTO;
import com.example.caliberclothing.entity.Payment;
import com.example.caliberclothing.entity.PaymentMethod;
import com.example.caliberclothing.entity.PaymentStatus;
import com.example.caliberclothing.repository.PaymentMethodRepository;
import com.example.caliberclothing.repository.PaymentRepository;
import com.example.caliberclothing.repository.PaymentStatusRepository;
import com.example.caliberclothing.service.PaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;

    public PaymentServiceImpl(PaymentRepository paymentRepository, PaymentMethodRepository paymentMethodRepository, PaymentStatusRepository paymentStatusRepository) {
        this.paymentRepository = paymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStatusRepository = paymentStatusRepository;
    }

    public PaymentDTO createPayment(PaymentDTO requestDto) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getPaymentMethod().getId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        PaymentStatus paymentStatus = paymentStatusRepository.findById(requestDto.getPaymentStatus().getId())
                .orElseThrow(() -> new RuntimeException("Payment status not found"));

        Payment payment = Payment.builder()
                .paymentNo(requestDto.getPaymentNo())
                .paymentDate(requestDto.getPaymentDate())
                .amountPaid(requestDto.getAmountPaid())
                .remarks(requestDto.getRemarks())
                .paymentReference(requestDto.getPaymentReference())
                .paymentMethod(paymentMethod)
                .paymentStatus(paymentStatus)
                .build();

        Payment savedPayment = paymentRepository.save(payment);
        return convertToResponseDTO(savedPayment);
    }

    public PaymentDTO updatePayment(Integer id, PaymentDTO requestDto) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getPaymentMethod().getId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        PaymentStatus paymentStatus = paymentStatusRepository.findById(requestDto.getPaymentStatus().getId())
                .orElseThrow(() -> new RuntimeException("Payment status not found"));

        payment.setPaymentNo(requestDto.getPaymentNo());
        payment.setPaymentDate(requestDto.getPaymentDate());
        payment.setAmountPaid(requestDto.getAmountPaid());
        payment.setRemarks(requestDto.getRemarks());
        payment.setPaymentReference(requestDto.getPaymentReference());
        payment.setPaymentMethod(paymentMethod);
        payment.setPaymentStatus(paymentStatus);

        Payment updatedPayment = paymentRepository.save(payment);
        return convertToResponseDTO(updatedPayment);
    }

    public PaymentDTO getPaymentById(Integer id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found"));
        return convertToResponseDTO(payment);
    }

    public List<PaymentDTO> getAllPayments() {
        List<Payment> payments = paymentRepository.findAll();
        return payments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByStatus(Integer statusId) {
        List<Payment> payments = paymentRepository.findByPaymentStatusId(statusId);
        return payments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByMethod(Integer methodId) {
        List<Payment> payments = paymentRepository.findByPaymentMethodId(methodId);
        return payments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Payment> payments = paymentRepository.findByPaymentDateBetween(startDate, endDate);
        return payments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deletePayment(Integer id) {
        if (!paymentRepository.existsById(id)) {
            throw new RuntimeException("Payment not found");
        }
        paymentRepository.deleteById(id);
    }

    private PaymentDTO convertToResponseDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .paymentNo(payment.getPaymentNo())
                .paymentDate(payment.getPaymentDate())
                .amountPaid(payment.getAmountPaid())
                .remarks(payment.getRemarks())
                .paymentReference(payment.getPaymentReference())
                .paymentMethod(convertToPaymentMethodDTO(payment.getPaymentMethod()))
                .paymentStatus(convertToPaymentStatusDTO(payment.getPaymentStatus()))
                .build();
    }


    private PaymentMethodDTO convertToPaymentMethodDTO(PaymentMethod method) {
        if (method == null) return null;
        return PaymentMethodDTO.builder()
                .id(method.getId())
                .value(method.getValue()) // or whatever the field is
                .build();
    }

    private PaymentStatusDTO convertToPaymentStatusDTO(PaymentStatus status) {
        if (status == null) return null;
        return PaymentStatusDTO.builder()
                .id(status.getId())
                .value(status.getValue()) // or whatever the field is
                .build();
    }

}
