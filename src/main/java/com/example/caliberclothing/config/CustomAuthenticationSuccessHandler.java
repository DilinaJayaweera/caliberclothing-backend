package com.example.caliberclothing.config;

import com.example.caliberclothing.service.impl.CustomUserDetails;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        String role = userDetails.getUser().getRole().toUpperCase();

        String redirectUrl = getRedirectUrl(role);

        Map<String, Object> data = new HashMap<>();
        data.put("success", true);
        data.put("message", "Login successful");
        data.put("role", role);
        data.put("redirectUrl", redirectUrl);
        data.put("username", userDetails.getUsername());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_OK);
        response.getWriter().write(objectMapper.writeValueAsString(data));
    }

    private String getRedirectUrl(String role) {
        return switch (role) {
            case "ADMIN" -> "/admin/dashboard";
            case "EMPLOYEE" -> "/employee/dashboard";
            case "CUSTOMER" -> "/customer/dashboard";
            default -> "/dashboard";
        };
    }
}

