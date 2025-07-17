package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, Integer> {
    Optional<PaymentMethod> findByValue(String value);
}
