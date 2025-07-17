package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.Delivery;
import com.example.caliberclothing.repository.DeliveryRepository;
import com.example.caliberclothing.service.DeliveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceImpl implements DeliveryService {

    @Autowired
    private DeliveryRepository repository;

    @Override
    public List<Delivery> getAllDeliveries() {
        return repository.findAll();
    }

    @Override
    public Optional<Delivery> getDeliveryById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<Delivery> getDeliveryByTrackingNo(String trackingNo) {
        return repository.findByTrackingNo(trackingNo);
    }

    @Override
    public List<Delivery> getDeliveriesByServiceProvider(Integer serviceProviderId) {
        return repository.findByDeliveryServiceProviderId(serviceProviderId);
    }

    @Override
    public List<Delivery> getDeliveriesByStatus(Integer statusId) {
        return repository.findByDeliveryStatusId(statusId);
    }

    @Override
    public List<Delivery> getDeliveriesByOrder(Integer orderId) {
        return repository.findByOrderId(orderId);
    }

    @Override
    public List<Delivery> getDeliveriesByExpectedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByExpectedDeliveryDateBetween(startDate, endDate);
    }

    @Override
    public List<Delivery> getDeliveriesByActualDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByActualDeliveryDateBetween(startDate, endDate);
    }

    @Override
    public List<Delivery> getDeliveriesByShippedDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return repository.findByShippedDateBetween(startDate, endDate);
    }

    @Override
    public Delivery createDelivery(Delivery delivery) {
        // Check if tracking number already exists
        if (repository.existsByTrackingNo(delivery.getTrackingNo())) {
            throw new RuntimeException("Delivery with this tracking number already exists");
        }

        // Set last updated timestamp
        delivery.setLastUpdatedTimestamp(LocalDateTime.now());

        return repository.save(delivery);
    }

    @Override
    public Delivery updateDelivery(Integer id, Delivery delivery) {
        Optional<Delivery> existingDelivery = repository.findById(id);

        if (!existingDelivery.isPresent()) {
            throw new RuntimeException("Delivery not found with id: " + id);
        }

        // Check if tracking number already exists for another delivery
        Optional<Delivery> trackingCheck = repository.findByTrackingNo(delivery.getTrackingNo());
        if (trackingCheck.isPresent() && !trackingCheck.get().getId().equals(id)) {
            throw new RuntimeException("Delivery with this tracking number already exists");
        }

        Delivery existing = existingDelivery.get();
        existing.setDeliveryServiceProvider(delivery.getDeliveryServiceProvider());
        existing.setShippedDate(delivery.getShippedDate());
        existing.setExpectedDeliveryDate(delivery.getExpectedDeliveryDate());
        existing.setActualDeliveryDate(delivery.getActualDeliveryDate());
        existing.setTrackingNo(delivery.getTrackingNo());
        existing.setDeliveryStatus(delivery.getDeliveryStatus());
        existing.setOrder(delivery.getOrder());
        existing.setLastUpdatedTimestamp(LocalDateTime.now());

        return repository.save(existing);
    }

    @Override
    public void deleteDelivery(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Delivery not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

