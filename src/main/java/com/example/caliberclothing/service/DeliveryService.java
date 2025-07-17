package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.Delivery;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface DeliveryService {

    List<Delivery> getAllDeliveries();

    Optional<Delivery> getDeliveryById(Integer id);

    Optional<Delivery> getDeliveryByTrackingNo(String trackingNo);

    List<Delivery> getDeliveriesByServiceProvider(Integer serviceProviderId);

    List<Delivery> getDeliveriesByStatus(Integer statusId);

    List<Delivery> getDeliveriesByOrder(Integer orderId);

    List<Delivery> getDeliveriesByExpectedDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Delivery> getDeliveriesByActualDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<Delivery> getDeliveriesByShippedDateRange(LocalDateTime startDate, LocalDateTime endDate);

    Delivery createDelivery(Delivery delivery);

    Delivery updateDelivery(Integer id, Delivery delivery);

    void deleteDelivery(Integer id);
}

