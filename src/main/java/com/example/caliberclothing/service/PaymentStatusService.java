package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.PaymentStatusDTO;
import com.example.caliberclothing.entity.PaymentStatus;

import java.util.List;
import java.util.stream.Collectors;

public interface PaymentStatusService {

    public List<PaymentStatusDTO> getAllPaymentStatuses();

    public PaymentStatusDTO getPaymentStatusById(Integer id);

}
