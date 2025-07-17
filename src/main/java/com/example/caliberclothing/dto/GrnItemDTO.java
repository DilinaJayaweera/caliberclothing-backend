package com.example.caliberclothing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class GrnItemDTO {

    private Integer id;
    private String quantityReceived;
    private String remarks;
    private BigDecimal unitCost;
    private BigDecimal subTotal;
    private GrnDTO grn;
    private ProductDTO product;

}
