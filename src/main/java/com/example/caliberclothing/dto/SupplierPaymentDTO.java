package com.example.caliberclothing.dto;

import com.example.caliberclothing.entity.PurchaseDetails;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
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
public class SupplierPaymentDTO {

    private Integer id;
    private LocalDateTime paymentDate;
    private BigDecimal amountPaid;
    private String referenceNo;
    private String remarks;
    private PurchaseDetails purchaseDetails;
}
