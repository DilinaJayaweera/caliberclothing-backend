package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    List<Cart> findByCustomerId(Integer customerId);

    Optional<Cart> findByCustomerIdAndProductId(Integer customerId, Integer productId);

    @Query("SELECT SUM(c.quantity) FROM Cart c WHERE c.customer.id = :customerId")
    Integer getTotalItemsInCart(@Param("customerId") Integer customerId);

    void deleteByCustomerId(Integer customerId);

    void deleteByCustomerIdAndProductId(Integer customerId, Integer productId);

    boolean existsByCustomerIdAndProductId(Integer customerId, Integer productId);
}