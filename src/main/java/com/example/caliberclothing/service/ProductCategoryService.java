package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.ProductCategory;

import java.util.List;
import java.util.Optional;

public interface ProductCategoryService {

    // Get all active categories
    List<ProductCategory> getAllActiveCategories();

    // Get all categories (including inactive)
    List<ProductCategory> getAllCategories();

    // Get category by ID
    Optional<ProductCategory> getCategoryById(Integer id);

    // Get category by category number
    Optional<ProductCategory> getCategoryByCategoryNo(String categoryNo);

    // Search categories by name
//    List<ProductCategory> searchCategoriesByName(String name);

    // Save new category
    ProductCategory saveCategory(ProductCategory category);

    // Update existing category
    ProductCategory updateCategory(ProductCategory category);

    // Soft delete category
    void softDeleteCategory(Integer id);

    // Hard delete category (use with caution)
    void deleteCategory(Integer id);

    // Generate category number
    String generateCategoryNo(String name);

    // Validate category name uniqueness
    boolean isCategoryNameUnique(String name);

    // Validate category name uniqueness for updates
    boolean isCategoryNameUniqueForUpdate(String name, Integer categoryId);

    // Get categories with product count
    List<ProductCategory> getCategoriesWithProductCount();

//    public List<ProductCategory> searchCategoriesByName(String name);
}