package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByIsActiveTrue();

    Optional<ProductCategory> findByCategoryNoAndIsActiveTrue(String categoryNo);

    List<ProductCategory> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    Optional<ProductCategory> findByIdAndIsActiveTrue(Integer id);
}
