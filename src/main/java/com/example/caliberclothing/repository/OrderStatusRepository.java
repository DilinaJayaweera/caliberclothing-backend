package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderStatusRepository extends JpaRepository<OrderStatus, Integer> {

    Optional<OrderStatus> findByValue(String value);

    boolean existsByValue(String value);
}