package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.ProductCategory;
import com.example.caliberclothing.repository.ProductCategoryRepository;
import com.example.caliberclothing.service.ProductCategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductCategoryServiceImpl implements ProductCategoryService {

    @Autowired
    private ProductCategoryRepository categoryRepository;

    public List<ProductCategory> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    public Optional<ProductCategory> getCategoryById(Integer id) {
        return categoryRepository.findByIdAndIsActiveTrue(id);
    }

    public Optional<ProductCategory> getCategoryByCategoryNo(String categoryNo) {
        return categoryRepository.findByCategoryNoAndIsActiveTrue(categoryNo);
    }

    public List<ProductCategory> searchCategoriesByName(String name) {
        return categoryRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    public ProductCategory saveCategory(ProductCategory category) {
        return categoryRepository.save(category);
    }

    public ProductCategory updateCategory(ProductCategory category) {
        category.setUpdatedTimestamp(LocalDateTime.now());
        return categoryRepository.save(category);
    }

    public void softDeleteCategory(Integer id) {
        Optional<ProductCategory> category = categoryRepository.findById(id);
        if (category.isPresent()) {
            ProductCategory c = category.get();
            c.setIsActive(false);
            c.setDeletedTimestamp(LocalDateTime.now());
            categoryRepository.save(c);
        }
    }
}
