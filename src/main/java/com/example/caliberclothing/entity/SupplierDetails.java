package com.example.caliberclothing.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "supplier_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class SupplierDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "supplier_no", nullable = false, unique = true)
    @NotBlank(message = "Supplier number is required")
    private String supplierNo;

    @Column(name = "supplier_name", nullable = false)
    @NotBlank(message = "Supplier name is required")
    private String supplierName;

    @Column(nullable = false)
    @NotBlank(message = "Country is required")
    private String country;

    @Column(nullable = false)
    @NotBlank(message = "Address is required")
    private String address;

    @Column(name = "contact_no", nullable = false)
    @NotBlank(message = "Contact number is required")
    private String contactNo;

    @Column(name = "created_timestamp", nullable = false)
//    @NotNull(message = "Created timestamp is required")
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp")
//    @NotNull(message = "Updated timestamp is required")
    private LocalDateTime updatedTimestamp;

    @Column(name = "deleted_timestamp")
    private LocalDateTime deletedTimestamp;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

}
