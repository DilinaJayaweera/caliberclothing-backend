package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.repository.ProductRepository;
import com.example.caliberclothing.service.ProductService;
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
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Transactional(readOnly = true)
    public List<Product> getAllActiveProducts() {
        List<Product> products = productRepository.findByIsActiveTrue();
        // Force initialization of lazy properties
        products.forEach(product -> {
            product.getSupplierDetails().getSupplierName(); // Access to initialize
            product.getProductCategory().getName(); // Access to initialize
        });
        return products;
    }

    public Optional<Product> getProductById(Integer id) {
        return productRepository.findById(id);
    }

    public Optional<Product> getActiveProductById(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        return product.filter(p -> p.getIsActive());
    }

    public Optional<Product> getProductByProductNo(String productNo) {
        return productRepository.findByProductNoAndIsActiveTrue(productNo);
    }

    public List<Product> searchProductsByName(String name) {
        return productRepository.findByNameContainingIgnoreCaseAndIsActiveTrue(name);
    }

    public List<Product> getProductsByCategory(Integer categoryId) {
        return productRepository.findByProductCategoryIdAndIsActiveTrue(categoryId);
    }

    public List<Product> getProductsBySupplier(Integer supplierId) {
        return productRepository.findBySupplierDetailsIdAndIsActiveTrue(supplierId);
    }

    public List<Product> getLowStockProducts(Integer threshold) {
        return productRepository.findLowStockProducts(threshold);
    }

    public Product saveProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Product product) {
        product.setUpdatedTimestamp(LocalDateTime.now());
        return productRepository.save(product);
    }

    public void softDeleteProduct(Integer id) {
        Optional<Product> product = productRepository.findById(id);
        if (product.isPresent()) {
            Product p = product.get();
            p.setIsActive(false);
            p.setDeletedTimestamp(LocalDateTime.now());
            productRepository.save(p);
        }
    }

    public void updateStock(Integer productId, Integer quantity) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product p = product.get();
            p.setQuantityInStock(quantity);
            updateProduct(p);
        }
    }
}
