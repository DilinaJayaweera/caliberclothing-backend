package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.DeliveryStatus;

import java.util.List;
import java.util.Optional;

public interface DeliveryStatusService {

    List<DeliveryStatus> getAllStatuses();

    Optional<DeliveryStatus> getStatusById(Integer id);

    Optional<DeliveryStatus> getStatusByValue(String value);

    DeliveryStatus createStatus(DeliveryStatus status);

    DeliveryStatus updateStatus(Integer id, DeliveryStatus status);

    void deleteStatus(Integer id);
}
