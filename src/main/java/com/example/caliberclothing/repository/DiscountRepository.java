package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Discount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DiscountRepository extends JpaRepository<Discount, Integer> {

    Optional<Discount> findByDiscountCode(String discountCode);

    List<Discount> findByIsActive(Boolean isActive);

    List<Discount> findByDiscountTypeId(Integer discountTypeId);

    @Query("SELECT d FROM Discount d WHERE d.isActive = true AND d.startDate <= :currentDate AND d.endDate >= :currentDate")
    List<Discount> findActiveDiscountsForDate(@Param("currentDate") LocalDate currentDate);

    @Query("SELECT d FROM Discount d WHERE d.discountCode = :code AND d.isActive = true AND d.startDate <= :currentDate AND d.endDate >= :currentDate")
    Optional<Discount> findActiveDiscountByCode(@Param("code") String code, @Param("currentDate") LocalDate currentDate);

    @Query("SELECT d FROM Discount d WHERE d.isActive = true AND d.startDate <= :currentDate AND d.endDate >= :currentDate AND (d.minimumOrderValue IS NULL OR d.minimumOrderValue <= :orderValue)")
    List<Discount> findApplicableDiscounts(@Param("currentDate") LocalDate currentDate, @Param("orderValue") BigDecimal orderValue);
}
