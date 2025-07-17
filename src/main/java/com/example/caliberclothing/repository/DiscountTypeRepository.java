package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.DiscountType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DiscountTypeRepository extends JpaRepository<DiscountType, Integer> {
    Optional<DiscountType> findByValue(String value);
}
