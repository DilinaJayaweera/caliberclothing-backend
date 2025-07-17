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
//@CrossOrigin(origins = "*")
public class DashboardController {

    @GetMapping("/admin/dashboard")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> getAdminDashboard(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Admin Dashboard");
        response.put("username", userDetails.getUsername());
        response.put("role", userDetails.getRole());
        response.put("dashboardType", "admin");
        response.put("permissions", new String[]{"user_management", "product_management", "order_management", "reports"});

        return ResponseEntity.ok(response);
    }

    @GetMapping("/employee/dashboard")
    @PreAuthorize("hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<?> getEmployeeDashboard(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Employee Dashboard");
        response.put("username", userDetails.getUsername());
        response.put("role", userDetails.getRole());
        response.put("dashboardType", "employee");
        response.put("permissions", new String[]{"product_management", "order_processing", "inventory_management"});

        return ResponseEntity.ok(response);
    }

    @GetMapping("/customer/dashboard")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('EMPLOYEE') or hasRole('ADMIN')")
    public ResponseEntity<?> getCustomerDashboard(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Customer Dashboard");
        response.put("username", userDetails.getUsername());
        response.put("role", userDetails.getRole());
        response.put("dashboardType", "customer");
        response.put("permissions", new String[]{"view_products", "place_orders", "view_order_history"});

        return ResponseEntity.ok(response);
    }

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDashboard(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String role = userDetails.getRole().toUpperCase();

            String redirectUrl = switch (role) {
                case "ADMIN" -> "/api/admin/dashboard";
                case "EMPLOYEE" -> "/api/employee/dashboard";
                case "CUSTOMER" -> "/api/customer/dashboard";
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
