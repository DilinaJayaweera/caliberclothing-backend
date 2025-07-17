package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.Discount;
import com.example.caliberclothing.entity.DiscountType;
import com.example.caliberclothing.repository.DiscountRepository;
import com.example.caliberclothing.repository.DiscountTypeRepository;
import com.example.caliberclothing.service.DiscountService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountServiceImpl implements DiscountService {

    private final DiscountRepository discountRepository;
    private final DiscountTypeRepository discountTypeRepository;

    public List<Discount> getAllDiscounts() {
        return discountRepository.findAll();
    }

    public Optional<Discount> getDiscountById(Integer id) {
        return discountRepository.findById(id);
    }

    public Optional<Discount> getDiscountByCode(String discountCode) {
        return discountRepository.findByDiscountCode(discountCode);
    }

    public List<Discount> getActiveDiscounts() {
        return discountRepository.findByIsActive(true);
    }

    public List<Discount> getDiscountsByType(Integer discountTypeId) {
        return discountRepository.findByDiscountTypeId(discountTypeId);
    }

    public List<Discount> getActiveDiscountsForDate(LocalDate date) {
        return discountRepository.findActiveDiscountsForDate(date);
    }

    public Optional<Discount> getActiveDiscountByCode(String code) {
        return discountRepository.findActiveDiscountByCode(code, LocalDate.now());
    }

    public List<Discount> getApplicableDiscounts(BigDecimal orderValue) {
        return discountRepository.findApplicableDiscounts(LocalDate.now(), orderValue);
    }

    public Discount createDiscount(Discount discount) {
        // Validate discount type exists
        if (discount.getDiscountType() != null && discount.getDiscountType().getId() != null) {
            Optional<DiscountType> discountType = discountTypeRepository.findById(discount.getDiscountType().getId());
            if (discountType.isEmpty()) {
                throw new RuntimeException("DiscountType not found with id: " + discount.getDiscountType().getId());
            }
            discount.setDiscountType(discountType.get());
        }

        // Validate dates
        if (discount.getStartDate().isAfter(discount.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        // Check if discount code already exists
        Optional<Discount> existingDiscount = discountRepository.findByDiscountCode(discount.getDiscountCode());
        if (existingDiscount.isPresent()) {
            throw new RuntimeException("Discount code already exists: " + discount.getDiscountCode());
        }

        return discountRepository.save(discount);
    }

    public Discount updateDiscount(Integer id, Discount discount) {
        Optional<Discount> existingDiscount = discountRepository.findById(id);
        if (existingDiscount.isEmpty()) {
            throw new RuntimeException("Discount not found with id: " + id);
        }

        // Validate discount type exists
        if (discount.getDiscountType() != null && discount.getDiscountType().getId() != null) {
            Optional<DiscountType> discountType = discountTypeRepository.findById(discount.getDiscountType().getId());
            if (discountType.isEmpty()) {
                throw new RuntimeException("DiscountType not found with id: " + discount.getDiscountType().getId());
            }
            discount.setDiscountType(discountType.get());
        }

        // Validate dates
        if (discount.getStartDate().isAfter(discount.getEndDate())) {
            throw new RuntimeException("Start date cannot be after end date");
        }

        // Check if discount code already exists for different discount
        Optional<Discount> existingDiscountWithCode = discountRepository.findByDiscountCode(discount.getDiscountCode());
        if (existingDiscountWithCode.isPresent() && !existingDiscountWithCode.get().getId().equals(id)) {
            throw new RuntimeException("Discount code already exists: " + discount.getDiscountCode());
        }

        discount.setId(id);
        return discountRepository.save(discount);
    }

    public void deleteDiscount(Integer id) {
        if (discountRepository.existsById(id)) {
            discountRepository.deleteById(id);
        } else {
            throw new RuntimeException("Discount not found with id: " + id);
        }
    }

    public void deactivateDiscount(Integer id) {
        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isPresent()) {
            Discount existingDiscount = discount.get();
            existingDiscount.setIsActive(false);
            discountRepository.save(existingDiscount);
        } else {
            throw new RuntimeException("Discount not found with id: " + id);
        }
    }

    public void activateDiscount(Integer id) {
        Optional<Discount> discount = discountRepository.findById(id);
        if (discount.isPresent()) {
            Discount existingDiscount = discount.get();
            existingDiscount.setIsActive(true);
            discountRepository.save(existingDiscount);
        } else {
            throw new RuntimeException("Discount not found with id: " + id);
        }
    }

    public boolean isDiscountValid(String discountCode, BigDecimal orderValue) {
        Optional<Discount> discount = getActiveDiscountByCode(discountCode);
        if (discount.isEmpty()) {
            return false;
        }

        Discount d = discount.get();

        // Check if minimum order value is met
        if (d.getMinimumOrderValue() != null && orderValue.compareTo(d.getMinimumOrderValue()) < 0) {
            return false;
        }

        return true;
    }
}
