package com.example.caliberclothing.entity;

import jakarta.persistence.*;
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
@Table(name = "`order`")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "order_no", nullable = false, unique = true)
    @NotBlank(message = "Order number is required")
    private String orderNo;

    @Column(nullable = false)
    @Min(value = 1, message = "Quantity must be at least 1")
    private Integer quantity;

    @Column(name = "unit_price", nullable = false)
    @NotNull(message = "Unit price is required")
    private BigDecimal unitPrice;

    @Column(name = "total_price", nullable = false)
    @NotNull(message = "Total price is required")
    private BigDecimal totalPrice;

    @Column(name = "shipping_address", nullable = false)
    @NotBlank(message = "Shipping address is required")
    private String shippingAddress;

    @Column(name = "order_date", nullable = false)
    @NotNull(message = "Order date is required")
    private LocalDateTime orderDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", nullable = false)
    private Customer customer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "employee_id", nullable = false)
    private Employee employee;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_status_id", nullable = false)
    private OrderStatus orderStatus;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "payment_id", nullable = false, unique = true)
    private Payment payment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "discount_id")
    private Discount discount;

}
