package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.ProductCategory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {

    public List<ProductCategory> getAllActiveCategories();

    public Optional<ProductCategory> getCategoryById(Integer id);

    public Optional<ProductCategory> getCategoryByCategoryNo(String categoryNo);

    public List<ProductCategory> searchCategoriesByName(String name);

    public ProductCategory saveCategory(ProductCategory category);

    public ProductCategory updateCategory(ProductCategory category);

    public void softDeleteCategory(Integer id);

}
