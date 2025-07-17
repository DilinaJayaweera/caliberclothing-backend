package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {

    List<WishList> findByCustomerId(Integer customerId);

    Optional<WishList> findByCustomerIdAndProductId(Integer customerId, Integer productId);

    void deleteByCustomerIdAndProductId(Integer customerId, Integer productId);

    boolean existsByCustomerIdAndProductId(Integer customerId, Integer productId);

    long countByCustomerId(Integer customerId);
}