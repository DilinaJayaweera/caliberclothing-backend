package com.example.caliberclothing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeDTO {

    private int id;
    private String employeeNo;
    private String fullName;
    private String firstName;
    private String lastName;
    private LocalDate dateOfBirth;
    private String sex;
    private String civilStatus;
    private String address;
    private String mobileNumber;
    private String telephoneNumber;
    private String nicNo;
    private Boolean isActive;
    private LocalDateTime createdTimestamp;
    private LocalDateTime updatedTimestamp;
    private LocalDateTime deletedTimestamp;
    private Integer createdUser;
    private Integer updatedUser;
    private Integer deletedUser;
//    private RoleDTO role;
    private StatusDTO status;
    private UserDTO user;
}
