package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Optional<Order> findByOrderNo(String orderNo);

    List<Order> findByCustomerId(Integer customerId);

    List<Order> findByEmployeeId(Integer employeeId);

    List<Order> findByOrderStatusId(Integer orderStatusId);

    List<Order> findByOrderDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT o FROM Order o WHERE o.totalPrice >= :minPrice AND o.totalPrice <= :maxPrice")
    List<Order> findByTotalPriceBetween(@Param("minPrice") BigDecimal minPrice,
                                        @Param("maxPrice") BigDecimal maxPrice);

    boolean existsByOrderNo(String orderNo);
}


