package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.DiscountType;
import com.example.caliberclothing.repository.DiscountTypeRepository;
import com.example.caliberclothing.service.DiscountTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DiscountTypeServiceImpl implements DiscountTypeService {

    private final DiscountTypeRepository discountTypeRepository;

    public List<DiscountType> getAllDiscountTypes() {
        return discountTypeRepository.findAll();
    }

    public Optional<DiscountType> getDiscountTypeById(Integer id) {
        return discountTypeRepository.findById(id);
    }

    public Optional<DiscountType> getDiscountTypeByValue(String value) {
        return discountTypeRepository.findByValue(value);
    }

    public DiscountType createDiscountType(DiscountType discountType) {
        return discountTypeRepository.save(discountType);
    }

    public DiscountType updateDiscountType(Integer id, DiscountType discountType) {
        if (discountTypeRepository.existsById(id)) {
            discountType.setId(id);
            return discountTypeRepository.save(discountType);
        }
        throw new RuntimeException("DiscountType not found with id: " + id);
    }

    public void deleteDiscountType(Integer id) {
        if (discountTypeRepository.existsById(id)) {
            discountTypeRepository.deleteById(id);
        } else {
            throw new RuntimeException("DiscountType not found with id: " + id);
        }
    }
}
