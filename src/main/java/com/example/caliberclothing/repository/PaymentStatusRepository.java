package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.PaymentStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentStatusRepository extends JpaRepository<PaymentStatus, Integer> {
    Optional<PaymentStatus> findByValue(String value);
}
