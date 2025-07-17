package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.SupplierPaymentDTO;
import com.example.caliberclothing.entity.PaymentMethod;
import com.example.caliberclothing.entity.PaymentStatus;
import com.example.caliberclothing.entity.PurchaseDetails;
import com.example.caliberclothing.entity.SupplierPayment;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public interface SupplierPaymentService {

    public SupplierPaymentDTO createSupplierPayment(SupplierPaymentDTO requestDto);

    public SupplierPaymentDTO updateSupplierPayment(Integer id, SupplierPaymentDTO requestDto);

    public SupplierPaymentDTO getSupplierPaymentById(Integer id);

    public List<SupplierPaymentDTO> getAllSupplierPayments();

    public List<SupplierPaymentDTO> getSupplierPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    public void deleteSupplierPayment(Integer id);

}
