package com.example.caliberclothing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@Table(name = "inventory")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Inventory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "reorder_level", nullable = false)
    @Min(value = 0, message = "Reorder level cannot be negative")
    private Integer reorderLevel;

    @Column(name = "total_quantity_purchasing", nullable = false)
    @Min(value = 0, message = "Total quantity cannot be negative")
    private Integer totalQuantityPurchasing;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

}
