package com.example.caliberclothing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class GrnDTO {

    private Integer id;
    private LocalDate receivedDate;
    private EmployeeDTO employee;
    private SupplierDetailsDTO supplierDetails;
    private GrnStatusDTO status;

}
