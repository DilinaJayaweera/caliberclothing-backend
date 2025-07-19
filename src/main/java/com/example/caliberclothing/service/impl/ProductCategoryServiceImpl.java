package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.ProductCategory;
import com.example.caliberclothing.repository.ProductCategoryRepository;
import com.example.caliberclothing.repository.ProductRepository;
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

    @Autowired
    private ProductRepository productRepository;

    @Override
    public List<ProductCategory> getAllActiveCategories() {
        return categoryRepository.findByIsActiveTrue();
    }

    @Override
    public List<ProductCategory> getAllCategories() {
        List<ProductCategory> categories = categoryRepository.findByIsActiveTrue();

        // Add product count for each category
        for (ProductCategory category : categories) {
            try {
                Long productCount = productRepository.countByProductCategoryIdAndIsActiveTrue(category.getId());
                category.setProductCount(productCount != null ? productCount : 0L);
            } catch (Exception e) {
                System.err.println("Error counting products for category " + category.getId() + ": " + e.getMessage());
                category.setProductCount(0L);
            }
        }

        return categories;
    }

    @Override
    public Optional<ProductCategory> getCategoryById(Integer id) {
        Optional<ProductCategory> categoryOpt = categoryRepository.findByIdAndIsActiveTrue(id);

        // Add product count if category exists
        if (categoryOpt.isPresent()) {
            ProductCategory category = categoryOpt.get();
            try {
                Long productCount = productRepository.countByProductCategoryIdAndIsActiveTrue(category.getId());
                category.setProductCount(productCount != null ? productCount : 0L);
            } catch (Exception e) {
                category.setProductCount(0L);
            }
        }

        return categoryOpt;
    }

    @Override
    public Optional<ProductCategory> getCategoryByCategoryNo(String categoryNo) {
        return categoryRepository.findByCategoryNoAndIsActiveTrue(categoryNo);
    }

//    @Override
//    public List<ProductCategory> searchCategoriesByName(String name) {
//        List<ProductCategory> categories = categoryRepository.findByNameOrDescriptionContainingIgnoreCaseAndIsActiveTrue(name);
//
//        // Add product count for each category in search results
//        for (ProductCategory category : categories) {
//            try {
//                Long productCount = productRepository.countByProductCategoryIdAndIsActiveTrue(category.getId());
//                category.setProductCount(productCount != null ? productCount : 0L);
//            } catch (Exception e) {
//                category.setProductCount(0L);
//            }
//        }
//
//        return categories;
//    }

    @Override
    public ProductCategory saveCategory(ProductCategory category) {
        // Generate category number if not provided
        if (category.getCategoryNo() == null || category.getCategoryNo().isEmpty()) {
            category.setCategoryNo(generateCategoryNo(category.getName()));
        }

        // Set timestamps
        category.setCreatedTimestamp(LocalDateTime.now());
        category.setUpdatedTimestamp(LocalDateTime.now());

        // Set as active by default
        if (category.getIsActive() == null) {
            category.setIsActive(true);
        }

        ProductCategory savedCategory = categoryRepository.save(category);

        // Set product count to 0 for new categories
        savedCategory.setProductCount(0L);

        return savedCategory;
    }

    @Override
    public ProductCategory updateCategory(ProductCategory category) {
        // Only update the timestamp
        category.setUpdatedTimestamp(LocalDateTime.now());
        ProductCategory updatedCategory = categoryRepository.save(category);

        // Add current product count
        try {
            Long productCount = productRepository.countByProductCategoryIdAndIsActiveTrue(updatedCategory.getId());
            updatedCategory.setProductCount(productCount != null ? productCount : 0L);
        } catch (Exception e) {
            updatedCategory.setProductCount(0L);
        }

        return updatedCategory;
    }

    @Override
    public void softDeleteCategory(Integer id) {
        Optional<ProductCategory> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            ProductCategory category = categoryOpt.get();

            // Check if category has active products
            try {
                Long productCount = productRepository.countByProductCategoryIdAndIsActiveTrue(id);
                if (productCount != null && productCount > 0) {
                    throw new RuntimeException("Cannot delete category with active products. Please remove or reassign products first.");
                }
            } catch (Exception e) {
                if (e.getMessage().contains("Cannot delete category")) {
                    throw e; // Re-throw our custom exception
                }
                System.err.println("Error checking product count for category deletion: " + e.getMessage());
            }

            category.setIsActive(false);
            category.setDeletedTimestamp(LocalDateTime.now());
            categoryRepository.save(category);
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    @Override
    public void deleteCategory(Integer id) {
        Optional<ProductCategory> categoryOpt = categoryRepository.findById(id);
        if (categoryOpt.isPresent()) {
            // Check if category has any products (active or inactive)
            try {
                Long productCount = productRepository.countByProductCategoryId(id);
                if (productCount != null && productCount > 0) {
                    throw new RuntimeException("Cannot delete category with products. Please remove all products first.");
                }
            } catch (Exception e) {
                if (e.getMessage().contains("Cannot delete category")) {
                    throw e; // Re-throw our custom exception
                }
                System.err.println("Error checking product count for category deletion: " + e.getMessage());
            }

            categoryRepository.deleteById(id);
        } else {
            throw new RuntimeException("Category not found with id: " + id);
        }
    }

    @Override
    public String generateCategoryNo(String name) {
        if (name == null || name.trim().isEmpty()) {
            return "CAT" + System.currentTimeMillis();
        }

        // Take first 3 characters of name and add timestamp
        String prefix = name.trim().toUpperCase().substring(0, Math.min(3, name.trim().length()));
        String timestamp = String.valueOf(System.currentTimeMillis()).substring(8); // Last 5 digits
        return prefix + timestamp;
    }

    @Override
    public boolean isCategoryNameUnique(String name) {
        return !categoryRepository.existsByNameAndIsActiveTrue(name);
    }

    @Override
    public boolean isCategoryNameUniqueForUpdate(String name, Integer categoryId) {
        return !categoryRepository.existsByNameAndIsActiveTrueAndIdNot(name, categoryId);
    }

    @Override
    public List<ProductCategory> getCategoriesWithProductCount() {
        return getAllCategories(); // This method already includes product count
    }
}