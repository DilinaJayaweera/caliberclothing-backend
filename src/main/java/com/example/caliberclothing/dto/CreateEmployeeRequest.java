package com.example.caliberclothing.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class CreateEmployeeRequest {

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

    @NotBlank(message = "Gender is required")
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

    @NotBlank(message = "Username is required")
    private String username;

    @NotBlank(message = "Password is required")
    private String password;

    @NotBlank(message = "Role is required")
    private String role;

    @NotNull(message = "Status ID is required")
    private Integer statusId;
}
