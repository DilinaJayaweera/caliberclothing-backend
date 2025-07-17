package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.Delivery;
import com.example.caliberclothing.service.DeliveryService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/deliveries")
//@CrossOrigin(origins = "*")
public class DeliveryController {

    @Autowired
    private DeliveryService service;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<Delivery>> getAllDeliveries() {
        try {
            List<Delivery> deliveries = service.getAllDeliveries();
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<Delivery> getDeliveryById(@PathVariable Integer id) {
        try {
            Optional<Delivery> delivery = service.getDeliveryById(id);
            return delivery.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/tracking/{trackingNo}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<Delivery> getDeliveryByTrackingNo(@PathVariable String trackingNo) {
        try {
            Optional<Delivery> delivery = service.getDeliveryByTrackingNo(trackingNo);
            return delivery.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/service-provider/{serviceProviderId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<Delivery>> getDeliveriesByServiceProvider(@PathVariable Integer serviceProviderId) {
        try {
            List<Delivery> deliveries = service.getDeliveriesByServiceProvider(serviceProviderId);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/status/{statusId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<Delivery>> getDeliveriesByStatus(@PathVariable Integer statusId) {
        try {
            List<Delivery> deliveries = service.getDeliveriesByStatus(statusId);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/order/{orderId}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<Delivery>> getDeliveriesByOrder(@PathVariable Integer orderId) {
        try {
            List<Delivery> deliveries = service.getDeliveriesByOrder(orderId);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/expected-date")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<Delivery>> getDeliveriesByExpectedDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Delivery> deliveries = service.getDeliveriesByExpectedDateRange(startDate, endDate);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/actual-date")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<Delivery>> getDeliveriesByActualDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Delivery> deliveries = service.getDeliveriesByActualDateRange(startDate, endDate);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/shipped-date")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<Delivery>> getDeliveriesByShippedDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<Delivery> deliveries = service.getDeliveriesByShippedDateRange(startDate, endDate);
            return ResponseEntity.ok(deliveries);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<Delivery> createDelivery(@Valid @RequestBody Delivery delivery) {
        try {
            Delivery createdDelivery = service.createDelivery(delivery);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdDelivery);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<Delivery> updateDelivery(@PathVariable Integer id,
                                                   @Valid @RequestBody Delivery delivery) {
        try {
            Delivery updatedDelivery = service.updateDelivery(id, delivery);
            return ResponseEntity.ok(updatedDelivery);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteDelivery(@PathVariable Integer id) {
        try {
            service.deleteDelivery(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
