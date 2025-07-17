package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.DeliveryServiceProvider;
import com.example.caliberclothing.service.DeliveryServiceProviderService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delivery-providers")
//@CrossOrigin(origins = "*")
public class DeliveryServiceProviderController {

    @Autowired
    private DeliveryServiceProviderService service;

    // Manager and Dispatch Officer can view all providers
    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<DeliveryServiceProvider>> getAllProviders() {
        try {
            List<DeliveryServiceProvider> providers = service.getAllProviders();
            return ResponseEntity.ok(providers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Manager and Dispatch Officer can view provider by ID
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<DeliveryServiceProvider> getProviderById(@PathVariable int id) {
        try {
            Optional<DeliveryServiceProvider> provider = service.getProviderById(id);
            return provider.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Only Manager can create providers
    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<DeliveryServiceProvider> createProvider(@Valid @RequestBody DeliveryServiceProvider provider) {
        try {
            DeliveryServiceProvider createdProvider = service.createProvider(provider);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdProvider);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Only Manager can update providers
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<DeliveryServiceProvider> updateProvider(@PathVariable int id,
                                                                  @Valid @RequestBody DeliveryServiceProvider provider) {
        try {
            DeliveryServiceProvider updatedProvider = service.updateProvider(id, provider);
            return ResponseEntity.ok(updatedProvider);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Only Manager can delete providers (soft delete)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteProvider(@PathVariable int id) {
        try {
            service.deleteProvider(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    // Manager and Dispatch Officer can view active providers
    @GetMapping("/active")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<DeliveryServiceProvider>> getActiveProviders() {
        try {
            List<DeliveryServiceProvider> providers = service.getActiveProviders();
            return ResponseEntity.ok(providers);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}