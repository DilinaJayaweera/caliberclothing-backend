package com.example.caliberclothing.dto;

import com.example.caliberclothing.entity.ProductCategory;
import com.example.caliberclothing.entity.SupplierDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class ProductDTO {

    private Integer id;
    private String productNo;
    private String name;
    private String description;
    private String productImage;
    private BigDecimal costPrice;
    private BigDecimal sellingPrice;
    private Integer quantityInStock;
    private BigDecimal profitPercentage;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
    private LocalDateTime deletedTimestamp;
    private Boolean isActive;
    private SupplierDetails supplierDetails;
    private ProductCategory productCategory;

}
