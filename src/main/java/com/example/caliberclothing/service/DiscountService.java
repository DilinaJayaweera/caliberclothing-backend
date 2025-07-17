package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.Discount;
import com.example.caliberclothing.entity.DiscountType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DiscountService {

    public List<Discount> getAllDiscounts();

    public Optional<Discount> getDiscountById(Integer id);

    public Optional<Discount> getDiscountByCode(String discountCode);

    public List<Discount> getActiveDiscounts();

    public List<Discount> getDiscountsByType(Integer discountTypeId);

    public List<Discount> getActiveDiscountsForDate(LocalDate date);

    public Optional<Discount> getActiveDiscountByCode(String code);

    public List<Discount> getApplicableDiscounts(BigDecimal orderValue);

    public Discount createDiscount(Discount discount);

    public Discount updateDiscount(Integer id, Discount discount);

    public void deleteDiscount(Integer id);

    public void deactivateDiscount(Integer id);

    public void activateDiscount(Integer id);

    public boolean isDiscountValid(String discountCode, BigDecimal orderValue);

}
