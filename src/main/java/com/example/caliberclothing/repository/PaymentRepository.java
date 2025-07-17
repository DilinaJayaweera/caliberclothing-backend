package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Integer> {
    List<Payment> findByPaymentStatusId(Integer statusId);
    List<Payment> findByPaymentMethodId(Integer methodId);
    Optional<Payment> findByPaymentNo(String paymentNo);
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);
}
