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

@Data
@Entity
@Table(name = "grn_item")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class GrnItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "quantity_received", nullable = false)
    @NotBlank(message = "Quantity received is required")
    private String quantityReceived;

    private String remarks;

    @Column(name = "unit_cost", nullable = false)
    @NotNull(message = "Unit cost is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid unit cost")
    private BigDecimal unitCost;

    @Column(name = "sub_total", nullable = false)
    @NotNull(message = "Sub total is required")
    @Digits(integer = 10, fraction = 2, message = "Invalid sub total")
    private BigDecimal subTotal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "grn_id", nullable = false)
    private Grn grn;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

}
