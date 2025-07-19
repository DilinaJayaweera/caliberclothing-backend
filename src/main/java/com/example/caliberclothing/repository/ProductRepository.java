package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    List<Product> findByIsActiveTrue();

    Optional<Product> findByProductNoAndIsActiveTrue(String productNo);

    List<Product> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    List<Product> findByProductCategoryIdAndIsActiveTrue(Integer categoryId);

    List<Product> findBySupplierDetailsIdAndIsActiveTrue(Integer supplierId);

    @Query("SELECT p FROM Product p WHERE p.quantityInStock <= :threshold AND p.isActive = true")
    List<Product> findLowStockProducts(@Param("threshold") Integer threshold);

    @Query("SELECT p FROM Product p WHERE p.isActive = true ORDER BY p.createdTimestamp DESC")
    List<Product> findAllActiveOrderByCreatedDesc();

    // Count active products in a specific category
//    Long countByProductCategoryIdAndIsActiveTrue(Integer categoryId);
//
//    // Count all products in a specific category (both active and inactive)
//    Long countByProductCategoryId(Integer categoryId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.productCategory.id = :categoryId AND p.isActive = true")
    Long countByProductCategoryIdAndIsActiveTrue(@Param("categoryId") Integer categoryId);

    @Query("SELECT COUNT(p) FROM Product p WHERE p.productCategory.id = :categoryId")
    Long countByProductCategoryId(@Param("categoryId") Integer categoryId);
}
