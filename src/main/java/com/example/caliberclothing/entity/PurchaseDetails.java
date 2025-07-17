package com.example.caliberclothing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "purchase_details")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "purchase_no", nullable = false, unique = true)
    @NotBlank(message = "Purchase number is required")
    private String purchaseNumber;

    @Column(nullable = false)
    @NotNull(message = "Date is required")
    private LocalDateTime date;

    @Column(nullable = false)
    @NotBlank(message = "Product information is required")
    private String product;

    @Column(name = "expected_delivery_date", nullable = false)
    @NotNull(message = "Expected delivery date is required")
    private LocalDate expectedDeliveryDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_id", nullable = false)
    private Grn grn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_details_id", nullable = false)
    private SupplierDetails supplierDetails;

}
