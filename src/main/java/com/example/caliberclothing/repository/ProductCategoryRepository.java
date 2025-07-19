package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Integer> {

    List<ProductCategory> findByIsActiveTrue();

    Optional<ProductCategory> findByCategoryNoAndIsActiveTrue(String categoryNo);

    List<ProductCategory> findByNameContainingIgnoreCaseAndIsActiveTrue(String name);

    Optional<ProductCategory> findByIdAndIsActiveTrue(Integer id);

    // New method to search by name or description
//    @Query("SELECT pc FROM ProductCategory pc WHERE pc.isActive = true AND " +
//            "(LOWER(pc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(pc.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
//    List<ProductCategory> findByNameOrDescriptionContainingIgnoreCaseAndIsActiveTrue(@Param("searchTerm") String searchTerm);

    // Method to get all categories including inactive ones (for admin purposes)
    List<ProductCategory> findAll();

    // Method to check if category name exists (for validation)
    boolean existsByNameAndIsActiveTrue(String name);

    // Method to check if category name exists excluding current category (for updates)
    @Query("SELECT COUNT(pc) > 0 FROM ProductCategory pc WHERE pc.name = :name AND pc.id != :id AND pc.isActive = true")
    boolean existsByNameAndIsActiveTrueAndIdNot(@Param("name") String name, @Param("id") Integer id);

    // Count active products in a category
//    Long countByProductCategoryIdAndIsActiveTrue(Integer categoryId);

    // Count all products in a category (active and inactive)
//    Long countByProductCategoryId(Integer categoryId);

//    @Query("SELECT pc FROM ProductCategory pc WHERE pc.isActive = true AND " +
//            "(LOWER(pc.name) LIKE LOWER(CONCAT('%', :searchTerm, '%')) OR " +
//            "LOWER(pc.description) LIKE LOWER(CONCAT('%', :searchTerm, '%')))")
//    List<ProductCategory> findByNameOrDescriptionContainingIgnoreCaseAndIsActiveTrue(@Param("searchTerm") String searchTerm);
}