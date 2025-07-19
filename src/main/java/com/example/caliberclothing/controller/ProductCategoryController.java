package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.ProductCategory;
import com.example.caliberclothing.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/categories")
//@CrossOrigin(origins = "*")
public class ProductCategoryController {

    @Autowired
    private ProductCategoryService categoryService;

    @GetMapping
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        List<ProductCategory> categories = categoryService.getAllActiveCategories();
        return ResponseEntity.ok(categories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Integer id) {
        Optional<ProductCategory> category = categoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/by-category-no/{categoryNo}")
    public ResponseEntity<ProductCategory> getCategoryByCategoryNo(@PathVariable String categoryNo) {
        Optional<ProductCategory> category = categoryService.getCategoryByCategoryNo(categoryNo);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

//    @GetMapping("/search")
//    public ResponseEntity<List<ProductCategory>> searchCategories(@RequestParam String name) {
//        List<ProductCategory> categories = categoryService.searchCategoriesByName(name);
//        return ResponseEntity.ok(categories);
//    }

    @PostMapping
    public ResponseEntity<ProductCategory> createCategory(@Valid @RequestBody ProductCategory category) {
        ProductCategory savedCategory = categoryService.saveCategory(category);
        return ResponseEntity.ok(savedCategory);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductCategory> updateCategory(@PathVariable Integer id, @Valid @RequestBody ProductCategory category) {
        Optional<ProductCategory> existingCategory = categoryService.getCategoryById(id);
        if (existingCategory.isPresent()) {
            category.setId(id);
            ProductCategory updatedCategory = categoryService.updateCategory(category);
            return ResponseEntity.ok(updatedCategory);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCategory(@PathVariable Integer id) {
        categoryService.softDeleteCategory(id);
        return ResponseEntity.noContent().build();
    }
}
