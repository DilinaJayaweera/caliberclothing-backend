package com.example.caliberclothing.dto;

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
public class DeliveryServiceProviderDTO {

    private int id;
    private String name;
    private String address;
    private String contactNo;
    private Boolean isActive;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
    private LocalDateTime deletedTimestamp;
    private String email;

}
