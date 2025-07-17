package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.PaymentMethodDTO;

import java.util.List;

public interface PaymentMethodService {

    public List<PaymentMethodDTO> getAllPaymentMethods();

    public PaymentMethodDTO getPaymentMethodById(Integer id);

}
