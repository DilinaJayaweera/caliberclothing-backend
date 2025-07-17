package com.example.caliberclothing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "delivery_service_provider")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryServiceProvider {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, unique = true)
    @NotBlank(message = "Name is required")
    private String name;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    @Column(name = "contact_no", nullable = false)
    @NotBlank(message = "Contact number is required")
    @Pattern(regexp = "\\d{10}", message = "Contact number must be exactly 10 digits")
    private String contactNo;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "created_timestamp", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp")
    @UpdateTimestamp
    private LocalDateTime updatedTimestamp;

    @Column(name = "deleted_timestamp")
    private LocalDateTime deletedTimestamp;

    @Column(nullable = false, unique = true)
    @Email(message = "Email should be valid")
    @NotBlank(message = "Email is required")
    private String email;

}
