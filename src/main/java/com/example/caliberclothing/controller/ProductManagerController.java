package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.entity.ProductCategory;
import com.example.caliberclothing.service.ProductService;
import com.example.caliberclothing.service.ProductCategoryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/product-manager")
@PreAuthorize("hasRole('PRODUCT_MANAGER')")
public class ProductManagerController {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductCategoryService productCategoryService;

    // Add this dashboard endpoint for login authentication
    @GetMapping("/dashboard")
    public ResponseEntity<String> getDashboard() {
        return ResponseEntity.ok("Product Manager Dashboard");
    }

    // Product endpoints
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Optional<Product> product = productService.getActiveProductById(id);
        return product.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        try {
            Product savedProduct = productService.saveProduct(product);
            return ResponseEntity.ok(savedProduct);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product product) {
        try {
            Optional<Product> existingProduct = productService.getProductById(id);
            if (existingProduct.isPresent()) {
                product.setId(id);
                Product updatedProduct = productService.updateProduct(product);
                return ResponseEntity.ok(updatedProduct);
            }
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        try {
            productService.softDeleteProduct(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Product>> searchProducts(@RequestParam String name) {
        List<Product> products = productService.searchProductsByName(name);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Integer categoryId) {
        List<Product> products = productService.getProductsByCategory(categoryId);
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/low-stock")
    public ResponseEntity<List<Product>> getLowStockProducts(@RequestParam(defaultValue = "10") Integer threshold) {
        List<Product> products = productService.getLowStockProducts(threshold);
        return ResponseEntity.ok(products);
    }

    @PutMapping("/products/{id}/stock")
    public ResponseEntity<Void> updateStock(@PathVariable Integer id, @RequestParam Integer quantity) {
        try {
            productService.updateStock(id, quantity);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Category Management endpoints
    @GetMapping("/categories")
    public ResponseEntity<List<ProductCategory>> getAllCategories() {
        try {
            List<ProductCategory> categories = productCategoryService.getAllCategories();
            return ResponseEntity.ok(categories);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/categories/{id}")
    public ResponseEntity<ProductCategory> getCategoryById(@PathVariable Integer id) {
        Optional<ProductCategory> category = productCategoryService.getCategoryById(id);
        return category.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/categories")
    public ResponseEntity<?> createCategory(@Valid @RequestBody ProductCategory category) {
        try {
            // Validate category name uniqueness
            if (!productCategoryService.isCategoryNameUnique(category.getName())) {
                return ResponseEntity.badRequest().body("Category name already exists");
            }

            ProductCategory savedCategory = productCategoryService.saveCategory(category);
            return ResponseEntity.ok(savedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to create category: " + e.getMessage());
        }
    }

    @PutMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable Integer id, @Valid @RequestBody ProductCategory category) {
        try {
            Optional<ProductCategory> existingCategory = productCategoryService.getCategoryById(id);
            if (existingCategory.isEmpty()) {
                return ResponseEntity.notFound().build();
            }

            // Validate category name uniqueness for update
            if (!productCategoryService.isCategoryNameUniqueForUpdate(category.getName(), id)) {
                return ResponseEntity.badRequest().body("Category name already exists");
            }

            // Preserve important fields from existing category
            ProductCategory existing = existingCategory.get();
            category.setId(id);
            category.setCategoryNo(existing.getCategoryNo());
            category.setCreatedTimestamp(existing.getCreatedTimestamp());
            category.setIsActive(existing.getIsActive());

            ProductCategory updatedCategory = productCategoryService.updateCategory(category);
            return ResponseEntity.ok(updatedCategory);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to update category: " + e.getMessage());
        }
    }

    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable Integer id) {
        try {
            productCategoryService.softDeleteCategory(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to delete category");
        }
    }

//    @GetMapping("/categories/search")
//    public ResponseEntity<List<ProductCategory>> searchCategories(@RequestParam String name) {
//        try {
//            List<ProductCategory> categories = productCategoryService.searchCategoriesByName(name);
//            return ResponseEntity.ok(categories);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }

    @GetMapping("/categories/validate-name")
    public ResponseEntity<Boolean> validateCategoryName(@RequestParam String name, @RequestParam(required = false) Integer excludeId) {
        try {
            boolean isUnique = excludeId != null ?
                    productCategoryService.isCategoryNameUniqueForUpdate(name, excludeId) :
                    productCategoryService.isCategoryNameUnique(name);
            return ResponseEntity.ok(isUnique);
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
}