package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.SupplierPaymentDTO;
import com.example.caliberclothing.entity.PaymentMethod;
import com.example.caliberclothing.entity.PaymentStatus;
import com.example.caliberclothing.entity.PurchaseDetails;
import com.example.caliberclothing.entity.SupplierPayment;
import com.example.caliberclothing.repository.PaymentMethodRepository;
import com.example.caliberclothing.repository.PaymentStatusRepository;
import com.example.caliberclothing.repository.PurchaseDetailsRepository;
import com.example.caliberclothing.repository.SupplierPaymentRepository;
import com.example.caliberclothing.service.SupplierPaymentService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class SupplierPaymentServiceImpl implements SupplierPaymentService {

    private final SupplierPaymentRepository supplierPaymentRepository;
    private final PaymentMethodRepository paymentMethodRepository;
    private final PaymentStatusRepository paymentStatusRepository;
    private final PurchaseDetailsRepository purchaseDetailsRepository;

    public SupplierPaymentServiceImpl(SupplierPaymentRepository supplierPaymentRepository,
                                  PaymentMethodRepository paymentMethodRepository,
                                  PaymentStatusRepository paymentStatusRepository,
                                  PurchaseDetailsRepository purchaseDetailsRepository) {
        this.supplierPaymentRepository = supplierPaymentRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.paymentStatusRepository = paymentStatusRepository;
        this.purchaseDetailsRepository = purchaseDetailsRepository;
    }

    public SupplierPaymentDTO createSupplierPayment(SupplierPaymentDTO requestDto) {
        PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        PaymentStatus paymentStatus = paymentStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Payment status not found"));

        PurchaseDetails purchaseDetails = purchaseDetailsRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Purchase details not found"));

        // Check if payment already exists for this purchase
        Optional<SupplierPayment> existingPayment = supplierPaymentRepository.findByPurchaseDetailsId(requestDto.getPurchaseDetails().getId());
        if (existingPayment.isPresent()) {
            throw new RuntimeException("Payment already exists for this purchase");
        }

        SupplierPayment supplierPayment = SupplierPayment.builder()
                .paymentDate(requestDto.getPaymentDate())
                .amountPaid(requestDto.getAmountPaid())
                .referenceNo(requestDto.getReferenceNo())
                .remarks(requestDto.getRemarks())
                .purchaseDetails(purchaseDetails)
                .build();


        SupplierPayment savedPayment = supplierPaymentRepository.save(supplierPayment);
        return convertToResponseDTO(savedPayment);
    }

    public SupplierPaymentDTO updateSupplierPayment(Integer id, SupplierPaymentDTO requestDto) {
        SupplierPayment supplierPayment = supplierPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier payment not found"));

        PaymentMethod paymentMethod = paymentMethodRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Payment method not found"));

        PaymentStatus paymentStatus = paymentStatusRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Payment status not found"));

        PurchaseDetails purchaseDetails = purchaseDetailsRepository.findById(requestDto.getId())
                .orElseThrow(() -> new RuntimeException("Purchase details not found"));

        // Check if another payment exists for this purchase (excluding current payment)
        Optional<SupplierPayment> existingPayment = supplierPaymentRepository.findByPurchaseDetailsId(requestDto.getPurchaseDetails().getId());
        if (existingPayment.isPresent() && !existingPayment.get().getId().equals(id)) {
            throw new RuntimeException("Payment already exists for this purchase");
        }

        supplierPayment.setPaymentDate(requestDto.getPaymentDate());
        supplierPayment.setAmountPaid(requestDto.getAmountPaid());
        supplierPayment.setReferenceNo(requestDto.getReferenceNo());
        supplierPayment.setRemarks(requestDto.getRemarks());
        supplierPayment.setPurchaseDetails(purchaseDetails);
//        supplierPayment.setPaymentMethod(paymentMethod);
//        supplierPayment.setPaymentStatus(paymentStatus);

        SupplierPayment updatedPayment = supplierPaymentRepository.save(supplierPayment);
        return convertToResponseDTO(updatedPayment);
    }

    public SupplierPaymentDTO getSupplierPaymentById(Integer id) {
        SupplierPayment supplierPayment = supplierPaymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Supplier payment not found"));
        return convertToResponseDTO(supplierPayment);
    }

    public List<SupplierPaymentDTO> getAllSupplierPayments() {
        List<SupplierPayment> payments = supplierPaymentRepository.findAll();
        return payments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

//    public List<SupplierPaymentDTO> getSupplierPaymentsByStatus(Integer statusId) {
//        List<SupplierPayment> payments = supplierPaymentRepository.findByPaymentStatusId(statusId);
//        return payments.stream()
//                .map(this::convertToResponseDTO)
//                .collect(Collectors.toList());
//    }

//    public List<SupplierPaymentResponseDto> getSupplierPaymentsByMethod(Integer methodId) {
//        List<SupplierPayment> payments = supplierPaymentRepository.findByPaymentMethodId(methodId);
//        return payments.stream()
//                .map(this::convertToResponseDTO)
//                .collect(Collectors.toList());
//    }

    public List<SupplierPaymentDTO> getSupplierPaymentsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<SupplierPayment> payments = supplierPaymentRepository.findByPaymentDateBetween(startDate, endDate);
        return payments.stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public void deleteSupplierPayment(Integer id) {
        if (!supplierPaymentRepository.existsById(id)) {
            throw new RuntimeException("Supplier payment not found");
        }
        supplierPaymentRepository.deleteById(id);
    }

    private SupplierPaymentDTO convertToResponseDTO(SupplierPayment payment) {
        return SupplierPaymentDTO.builder()
                .id(payment.getId())
                .paymentDate(payment.getPaymentDate())
                .amountPaid(payment.getAmountPaid())
                .referenceNo(payment.getReferenceNo())
                .remarks(payment.getRemarks())
                .purchaseDetails(payment.getPurchaseDetails())
//                .paymentMethodValue(payment.getPaymentMethod().getValue())
//                .paymentStatusValue(payment.getPaymentStatus().getValue())
                .build();
    }
}

