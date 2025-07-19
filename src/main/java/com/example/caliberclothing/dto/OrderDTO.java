package com.example.caliberclothing.dto;

import com.example.caliberclothing.entity.Employee;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class OrderDTO {

    private Integer id;
    private String orderNo;
    private Integer quantity;
    private BigDecimal unitPrice;
    private BigDecimal totalPrice;
    private String shippingAddress;
    private LocalDateTime orderDate;
    private CustomerDTO customer;
//    private Employee employee;
    private OrderStatusDTO orderStatus;
//    private PaymentDTO payment;
//    private DiscountDTO discount;

}
