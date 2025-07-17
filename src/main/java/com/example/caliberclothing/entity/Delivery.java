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
@Table(name = "delivery")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Delivery {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_service_provider_id", nullable = false)
    private DeliveryServiceProvider deliveryServiceProvider;

    @Column(name = "shipped_date", nullable = false)
    @NotNull
    private LocalDateTime shippedDate;

    @Column(name = "expected_delivery_date", nullable = false)
    @NotNull
    private LocalDateTime expectedDeliveryDate;

    @Column(name = "actual_delivery_date", nullable = false)
    @NotNull
    private LocalDateTime actualDeliveryDate;

    @Column(name = "tracking_no", nullable = false)
    @NotBlank
    private String trackingNo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "delivery_status_id", nullable = false)
    private DeliveryStatus deliveryStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Column(name = "last_updated_timestamp", nullable = false)
    @NotNull
    private LocalDateTime lastUpdatedTimestamp;

}
