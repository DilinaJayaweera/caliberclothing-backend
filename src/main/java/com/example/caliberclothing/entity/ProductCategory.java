package com.example.caliberclothing.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "product_category")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "category_no", nullable = false)
    private String categoryNo;

    @Column(nullable = false)
    @NotBlank(message = "Name is required")
    private String name;

//    @Column(name = "description")
//    private String description;

    @Column(name = "created_timestamp", nullable = false)
    private LocalDateTime createdTimestamp;

    @Column(name = "updated_timestamp")
    private LocalDateTime updatedTimestamp;

    @Column(name = "deleted_timestamp")
    private LocalDateTime deletedTimestamp;

    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    // Transient field to hold product count (not persisted to database)
    @Transient
    private Long productCount;

}