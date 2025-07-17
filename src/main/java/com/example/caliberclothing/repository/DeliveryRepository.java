package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryRepository extends JpaRepository<Delivery, Integer> {

    Optional<Delivery> findByTrackingNo(String trackingNo);

    List<Delivery> findByDeliveryServiceProviderId(Integer deliveryServiceProviderId);

    List<Delivery> findByDeliveryStatusId(Integer deliveryStatusId);

    List<Delivery> findByOrderId(Integer orderId);

    @Query("SELECT d FROM Delivery d WHERE d.expectedDeliveryDate BETWEEN :startDate AND :endDate")
    List<Delivery> findByExpectedDeliveryDateBetween(@Param("startDate") LocalDateTime startDate,
                                                     @Param("endDate") LocalDateTime endDate);

    @Query("SELECT d FROM Delivery d WHERE d.actualDeliveryDate BETWEEN :startDate AND :endDate")
    List<Delivery> findByActualDeliveryDateBetween(@Param("startDate") LocalDateTime startDate,
                                                   @Param("endDate") LocalDateTime endDate);

    @Query("SELECT d FROM Delivery d WHERE d.shippedDate BETWEEN :startDate AND :endDate")
    List<Delivery> findByShippedDateBetween(@Param("startDate") LocalDateTime startDate,
                                            @Param("endDate") LocalDateTime endDate);

    boolean existsByTrackingNo(String trackingNo);
}
