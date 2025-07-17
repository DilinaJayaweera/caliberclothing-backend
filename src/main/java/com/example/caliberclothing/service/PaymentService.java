package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.PaymentDTO;
import com.example.caliberclothing.dto.PaymentMethodDTO;
import com.example.caliberclothing.dto.PaymentStatusDTO;
import com.example.caliberclothing.entity.Payment;
import com.example.caliberclothing.entity.PaymentMethod;
import com.example.caliberclothing.entity.PaymentStatus;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public interface PaymentService {

    public PaymentDTO createPayment(PaymentDTO requestDto);

    public PaymentDTO updatePayment(Integer id, PaymentDTO requestDto);

    public PaymentDTO getPaymentById(Integer id);

    public List<PaymentDTO> getAllPayments();

    public List<PaymentDTO> getPaymentsByStatus(Integer statusId);

    public List<PaymentDTO> getPaymentsByMethod(Integer methodId);

    public List<PaymentDTO> getPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public void deletePayment(Integer id);

}
