package com.example.caliberclothing.service;

import com.example.caliberclothing.entity.DeliveryServiceProvider;

import java.util.List;
import java.util.Optional;

public interface DeliveryServiceProviderService {

    List<DeliveryServiceProvider> getAllProviders();

    Optional<DeliveryServiceProvider> getProviderById(int id);

    DeliveryServiceProvider createProvider(DeliveryServiceProvider provider);

    DeliveryServiceProvider updateProvider(int id, DeliveryServiceProvider provider);

    void deleteProvider(int id);

    List<DeliveryServiceProvider> getActiveProviders();
}
