package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.entity.DeliveryServiceProvider;
import com.example.caliberclothing.repository.DeliveryServiceProviderRepository;
import com.example.caliberclothing.service.DeliveryServiceProviderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class DeliveryServiceProviderServiceImpl implements DeliveryServiceProviderService {

    @Autowired
    private DeliveryServiceProviderRepository repository;

    @Override
    public List<DeliveryServiceProvider> getAllProviders() {
        return repository.findAllActive();
    }

    @Override
    public Optional<DeliveryServiceProvider> getProviderById(int id) {
        return repository.findByIdAndNotDeleted(id);
    }

    @Override
    public DeliveryServiceProvider createProvider(DeliveryServiceProvider provider) {
        // Check if name already exists
        if (repository.findByNameAndNotDeleted(provider.getName()).isPresent()) {
            throw new RuntimeException("Provider with this name already exists");
        }

        // Check if email already exists
        if (repository.findByEmailAndNotDeleted(provider.getEmail()).isPresent()) {
            throw new RuntimeException("Provider with this email already exists");
        }

        // Set default values
        if (provider.getIsActive() == null) {
            provider.setIsActive(true);
        }

        return repository.save(provider);
    }

    @Override
    public DeliveryServiceProvider updateProvider(int id, DeliveryServiceProvider provider) {
        Optional<DeliveryServiceProvider> existingProvider = repository.findByIdAndNotDeleted(id);

        if (!existingProvider.isPresent()) {
            throw new RuntimeException("Provider not found with id: " + id);
        }

        DeliveryServiceProvider existing = existingProvider.get();

        // Check if name already exists for another provider
        Optional<DeliveryServiceProvider> nameCheck = repository.findByNameAndNotDeleted(provider.getName());
        if (nameCheck.isPresent() && nameCheck.get().getId() != id) {
            throw new RuntimeException("Provider with this name already exists");
        }

        // Check if email already exists for another provider
        Optional<DeliveryServiceProvider> emailCheck = repository.findByEmailAndNotDeleted(provider.getEmail());
        if (emailCheck.isPresent() && emailCheck.get().getId() != id) {
            throw new RuntimeException("Provider with this email already exists");
        }

        // Update fields
        existing.setName(provider.getName());
        existing.setAddress(provider.getAddress());
        existing.setContactNo(provider.getContactNo());
        existing.setEmail(provider.getEmail());
        existing.setIsActive(provider.getIsActive());

        return repository.save(existing);
    }

    @Override
    public void deleteProvider(int id) {
        Optional<DeliveryServiceProvider> provider = repository.findByIdAndNotDeleted(id);

        if (!provider.isPresent()) {
            throw new RuntimeException("Provider not found with id: " + id);
        }

        DeliveryServiceProvider existing = provider.get();
        existing.setDeletedTimestamp(LocalDateTime.now());
        repository.save(existing);
    }

    @Override
    public List<DeliveryServiceProvider> getActiveProviders() {
        return repository.findAllActiveProviders();
    }
}
