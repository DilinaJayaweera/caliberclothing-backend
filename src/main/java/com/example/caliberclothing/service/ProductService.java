package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {

    public List<Product> getAllActiveProducts();

    public Optional<Product> getProductById(Integer id);

    public Optional<Product> getActiveProductById(Integer id);

    public Optional<Product> getProductByProductNo(String productNo);

    public List<Product> searchProductsByName(String name);

    public List<Product> getProductsByCategory(Integer categoryId);

    public List<Product> getProductsBySupplier(Integer supplierId);

    public List<Product> getLowStockProducts(Integer threshold);

    public Product saveProduct(Product product);

    public Product updateProduct(Product product);

    public void softDeleteProduct(Integer id);

    public void updateStock(Integer productId, Integer quantity);

}
