package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.DeliveryStatus;
import com.example.caliberclothing.repository.DeliveryStatusRepository;
import com.example.caliberclothing.service.DeliveryStatusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class DeliveryStatusServiceImpl implements DeliveryStatusService {

    @Autowired
    private DeliveryStatusRepository repository;

    @Override
    public List<DeliveryStatus> getAllStatuses() {
        return repository.findAll();
    }

    @Override
    public Optional<DeliveryStatus> getStatusById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public Optional<DeliveryStatus> getStatusByValue(String value) {
        return repository.findByValue(value);
    }

    @Override
    public DeliveryStatus createStatus(DeliveryStatus status) {
        if (repository.existsByValue(status.getValue())) {
            throw new RuntimeException("Delivery status with this value already exists");
        }
        return repository.save(status);
    }

    @Override
    public DeliveryStatus updateStatus(Integer id, DeliveryStatus status) {
        Optional<DeliveryStatus> existingStatus = repository.findById(id);

        if (!existingStatus.isPresent()) {
            throw new RuntimeException("Delivery status not found with id: " + id);
        }

        Optional<DeliveryStatus> valueCheck = repository.findByValue(status.getValue());
        if (valueCheck.isPresent() && !Objects.equals(valueCheck.get().getId(), id)) {
            throw new RuntimeException("Delivery status with this value already exists");
        }

        DeliveryStatus existing = existingStatus.get();
        existing.setValue(status.getValue());

        return repository.save(existing);
    }


    @Override
    public void deleteStatus(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Delivery status not found with id: " + id);
        }
        repository.deleteById(id);
    }
}

