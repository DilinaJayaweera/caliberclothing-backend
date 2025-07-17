package com.example.caliberclothing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class DiscountDTO {

    private Integer id;
    private String discountCode;
    private String description;
    private BigDecimal discountValue;
    private LocalDate startDate;
    private LocalDate endDate;
    private BigDecimal minimumOrderValue;
    private Boolean isActive;
    private DiscountTypeDTO discountType;

}
