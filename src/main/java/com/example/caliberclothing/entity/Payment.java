package com.example.caliberclothing.entity;

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
@Table(name = "payment")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "payment_no", nullable = false)
    @NotBlank(message = "Payment number is required")
    private String paymentNo;

    @Column(name = "payment_date", nullable = false)
    @NotNull(message = "Payment date is required")
    private LocalDateTime paymentDate;

    @Column(name = "amount_paid", nullable = false)
    @NotBlank(message = "Amount paid is required")
    private String amountPaid;

    private String remarks;

    @Column(name = "payment_reference", nullable = false)
    @NotBlank(message = "Payment reference is required")
    private String paymentReference;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_method_id", nullable = false)
    private PaymentMethod paymentMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_status_id", nullable = false)
    private PaymentStatus paymentStatus;

}
