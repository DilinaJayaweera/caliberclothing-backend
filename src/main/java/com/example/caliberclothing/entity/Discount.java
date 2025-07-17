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
import java.time.LocalDate;

@Data
@Entity
@Table(name = "discount")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Discount {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "discount_code", nullable = false)
    @NotBlank(message = "Discount code is required")
    private String discountCode;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "discount_value", nullable = false)
    @NotNull(message = "Discount value is required")
    @Digits(integer = 8, fraction = 2, message = "Invalid discount value")
    private BigDecimal discountValue;

    @Column(name = "start_date", nullable = false)
    @NotNull(message = "Start date is required")
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    @NotNull(message = "End date is required")
    private LocalDate endDate;

    @Column(name = "minimum_order_value")
    @Digits(integer = 8, fraction = 2, message = "Invalid minimum order value")
    private BigDecimal minimumOrderValue;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_type_id", nullable = false)
    private DiscountType discountType;

}
