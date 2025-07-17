package com.example.caliberclothing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {

    private Integer id;
    private String paymentNo;
    private LocalDateTime paymentDate;
    private String amountPaid;
    private String remarks;
    private String paymentReference;
    private PaymentMethodDTO paymentMethod;
    private PaymentStatusDTO paymentStatus;

}
