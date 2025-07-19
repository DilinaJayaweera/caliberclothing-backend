package com.example.caliberclothing.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
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
@Table(name = "product")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "product_no", nullable = false, unique = true)
    @NotBlank(message = "Product number is required")
    private String productNo;

    @Column(nullable = false)
    @NotBlank(message = "Product name is required")
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(name = "product_image", nullable = false)
    private String productImage;

    @Column(name = "cost_price", nullable = false)
    @NotNull(message = "Cost price is required")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal costPrice;

    @Column(name = "selling_price", nullable = false)
    @NotNull(message = "Selling price is required")
    @Digits(integer = 8, fraction = 2)
    private BigDecimal sellingPrice;

    @Column(name = "quantity_in_stock", nullable = false)
    @Min(value = 0, message = "Quantity cannot be negative")
    private Integer quantityInStock;

    @Column(name = "profit_percentage", nullable = false)
    @Digits(integer = 3, fraction = 2)
    private BigDecimal profitPercentage;

    @Column(name = "created_timestamp", nullable = false)
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @Column(name = "deleted_timestamp")
    private LocalDateTime deletedTimestamp;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_details_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private SupplierDetails supplierDetails;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_category_id", nullable = false)
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    private ProductCategory productCategory;

}
