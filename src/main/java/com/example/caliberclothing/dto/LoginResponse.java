package com.example.caliberclothing.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    private boolean success;
    private String message;
    private String role;
    private String redirectUrl;
    private String username;
    private String token; // For JWT implementation if needed
}
