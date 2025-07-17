package com.example.caliberclothing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.persistence.Column;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class SupplierDetailsDTO {

    private Integer id;
    private String supplierNo;
    private String supplierName;
    private String country;
    private String address;
    private String contactNo;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
    private LocalDateTime deletedTimestamp;
    private Boolean isActive;

}
