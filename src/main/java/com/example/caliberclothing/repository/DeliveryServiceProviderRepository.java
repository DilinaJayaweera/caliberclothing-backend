package com.example.caliberclothing.repository;

import com.example.caliberclothing.entity.DeliveryServiceProvider;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DeliveryServiceProviderRepository extends JpaRepository<DeliveryServiceProvider, Integer> {

    @Query("SELECT d FROM DeliveryServiceProvider d WHERE d.deletedTimestamp IS NULL")
    List<DeliveryServiceProvider> findAllActive();

    @Query("SELECT d FROM DeliveryServiceProvider d WHERE d.id = :id AND d.deletedTimestamp IS NULL")
    Optional<DeliveryServiceProvider> findByIdAndNotDeleted(int id);

    @Query("SELECT d FROM DeliveryServiceProvider d WHERE d.name = :name AND d.deletedTimestamp IS NULL")
    Optional<DeliveryServiceProvider> findByNameAndNotDeleted(String name);

    @Query("SELECT d FROM DeliveryServiceProvider d WHERE d.email = :email AND d.deletedTimestamp IS NULL")
    Optional<DeliveryServiceProvider> findByEmailAndNotDeleted(String email);

    @Query("SELECT d FROM DeliveryServiceProvider d WHERE d.isActive = true AND d.deletedTimestamp IS NULL")
    List<DeliveryServiceProvider> findAllActiveProviders();
}

