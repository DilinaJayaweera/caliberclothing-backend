package com.example.caliberclothing.dto;

import com.example.caliberclothing.entity.DeliveryServiceProvider;
import com.example.caliberclothing.entity.DeliveryStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDateTime;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryDTO {

    private Integer id;
    private DeliveryServiceProvider deliveryServiceProvider;
    private LocalDateTime shippedDate;
    private LocalDateTime expectedDeliveryDate;
    private LocalDateTime actualDeliveryDate;
    private String trackingNo;
    private DeliveryStatus deliveryStatus;
    private OrderDTO order;
    private LocalDateTime lastUpdatedTimestamp;

}
