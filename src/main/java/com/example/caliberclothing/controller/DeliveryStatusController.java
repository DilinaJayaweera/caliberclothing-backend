package com.example.caliberclothing.controller;

import com.example.caliberclothing.entity.DeliveryStatus;
import com.example.caliberclothing.service.DeliveryStatusService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/delivery-status")
//@CrossOrigin(origins = "*")
public class DeliveryStatusController {

    @Autowired
    private DeliveryStatusService service;

    @GetMapping
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<List<DeliveryStatus>> getAllStatuses() {
        try {
            List<DeliveryStatus> statuses = service.getAllStatuses();
            return ResponseEntity.ok(statuses);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<DeliveryStatus> getStatusById(@PathVariable Integer id) {
        try {
            Optional<DeliveryStatus> status = service.getStatusById(id);
            return status.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/value/{value}")
    @PreAuthorize("hasRole('MANAGER') or hasRole('DISPATCH_OFFICER')")
    public ResponseEntity<DeliveryStatus> getStatusByValue(@PathVariable String value) {
        try {
            Optional<DeliveryStatus> status = service.getStatusByValue(value);
            return status.map(ResponseEntity::ok)
                    .orElse(ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<DeliveryStatus> createStatus(@Valid @RequestBody DeliveryStatus status) {
        try {
            DeliveryStatus createdStatus = service.createStatus(status);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<DeliveryStatus> updateStatus(@PathVariable Integer id,
                                                       @Valid @RequestBody DeliveryStatus status) {
        try {
            DeliveryStatus updatedStatus = service.updateStatus(id, status);
            return ResponseEntity.ok(updatedStatus);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('MANAGER')")
    public ResponseEntity<Void> deleteStatus(@PathVariable Integer id) {
        try {
            service.deleteStatus(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
