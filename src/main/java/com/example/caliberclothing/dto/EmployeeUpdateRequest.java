package com.example.caliberclothing.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Getter
@Setter
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
@AllArgsConstructor
@NoArgsConstructor
public class EmployeeUpdateRequest {
    @NotBlank(message = "Employee number is required")
    private String employeeNo;

    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @NotNull(message = "Date of birth is required")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Sex is required")
    private String sex;

    @NotBlank(message = "Civil status is required")
    private String civilStatus;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Mobile number is required")
    private String mobileNumber;

    private String telephoneNumber;

    @NotBlank(message = "NIC number is required")
    private String nicNo;

    private String username;
    private String password;

    @NotNull(message = "Status ID is required")
    private Integer statusId;  // <-- This field was missing!

    private Boolean isActive;

    @NotBlank(message = "Role is required")
    private String role;
}
