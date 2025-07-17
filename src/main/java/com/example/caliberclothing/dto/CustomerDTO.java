package com.example.caliberclothing.dto;

import com.example.caliberclothing.entity.Status;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Setter
@Getter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {

    private Integer id;
    private String fullName;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String sex;
    private String email;
//    private String civilStatus;
    private String address;
    private String country;
    private String zipCode;
    private String mobileNumber;
    private String nicNo;
    private Boolean isActive;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
    private LocalDateTime deletedTimestamp;
    private StatusDTO status;
    private UserDTO user;
    private ProvinceDTO province;


}
