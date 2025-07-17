package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.DiscountType;

import java.util.List;
import java.util.Optional;

public interface DiscountTypeService {

    public List<DiscountType> getAllDiscountTypes();

    public Optional<DiscountType> getDiscountTypeById(Integer id);

    public Optional<DiscountType> getDiscountTypeByValue(String value);

    public DiscountType createDiscountType(DiscountType discountType);

    public DiscountType updateDiscountType(Integer id, DiscountType discountType);

    public void deleteDiscountType(Integer id);

}
