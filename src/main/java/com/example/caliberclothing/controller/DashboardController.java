package com.example.caliberclothing.controller;

import com.example.caliberclothing.service.impl.CustomUserDetails;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class DashboardController {

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String role = userDetails.getRole().toUpperCase();

            String redirectUrl = switch (role) {
                case "CEO" -> "/api/ceo/dashboard";
                case "PRODUCT_MANAGER" -> "/api/product-manager/dashboard";
                case "MERCHANDISE_MANAGER" -> "/api/merchandise-manager/dashboard";
                case "DISPATCH_OFFICER" -> "/api/dispatch-officer/dashboard";
                case "CUSTOMER" -> "/products"; // Redirect to products page for customers
                default -> "/api/dashboard";
            };

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Redirecting to appropriate dashboard");
            response.put("redirectUrl", redirectUrl);
            response.put("role", role);

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
    }
}