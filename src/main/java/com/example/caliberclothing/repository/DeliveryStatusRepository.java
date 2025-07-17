package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.DeliveryStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DeliveryStatusRepository extends JpaRepository<DeliveryStatus, Integer> {

    Optional<DeliveryStatus> findByValue(String value);

    boolean existsByValue(String value);
}
