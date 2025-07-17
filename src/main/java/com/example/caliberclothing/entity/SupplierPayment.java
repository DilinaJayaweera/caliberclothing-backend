package com.example.caliberclothing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "supplier_payment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SupplierPayment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_date", nullable = false)
    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;

    @Column(name = "amount_paid", nullable = false)
    @NotNull(message = "Amount paid is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid amount format")
    private BigDecimal amountPaid;

    @Column(name = "reference_no", nullable = false, length = 100)
    @NotBlank(message = "Reference number is required")
    private String referenceNo;

    @Column(name = "remarks", length = 2000)
    private String remarks;

    @OneToOne
    @JoinColumn(name = "purchase_details_id", nullable = false, unique = true)
    private PurchaseDetails purchaseDetails;

}
