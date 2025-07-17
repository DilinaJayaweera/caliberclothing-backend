package com.example.caliberclothing.dto;

import lombok.Data;

@Data
public class RegisterRequest {

//    @NotBlank(message = "Username is required")
    private String username;

//    @NotBlank(message = "Password is required")
    private String password;

//    @NotBlank(message = "Role is required")
//    @Pattern(regexp = "^(ADMIN|EMPLOYEE|CUSTOMER)$", message = "Role must be ADMIN, EMPLOYEE, or CUSTOMER")
    private String role;
}
