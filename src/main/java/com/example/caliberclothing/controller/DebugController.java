package com.example.caliberclothing.controller;

import com.example.caliberclothing.service.impl.CustomUserDetails;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    private static final Logger logger = LoggerFactory.getLogger(DebugController.class);

    @GetMapping("/auth")
    public ResponseEntity<Map<String, Object>> debugAuth() {
        logger.info("=== DEBUG AUTH ENDPOINT CALLED ===");

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> debug = new HashMap<>();

        if (auth == null) {
            logger.error("No authentication found in SecurityContext");
            debug.put("error", "No authentication");
            return ResponseEntity.ok(debug);
        }

        logger.info("Authentication class: {}", auth.getClass().getName());
        logger.info("Is authenticated: {}", auth.isAuthenticated());
        logger.info("Principal class: {}", auth.getPrincipal().getClass().getName());
        logger.info("Authorities: {}", auth.getAuthorities());

        debug.put("authenticated", auth.isAuthenticated());
        debug.put("principal", auth.getPrincipal().toString());
        debug.put("authorities", auth.getAuthorities().toString());

        if (auth.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) auth.getPrincipal();
            logger.info("Username: {}", userDetails.getUsername());
            logger.info("Role: {}", userDetails.getRole());
            logger.info("User ID: {}", userDetails.getUser().getId());

            debug.put("username", userDetails.getUsername());
            debug.put("role", userDetails.getRole());
            debug.put("userId", userDetails.getUser().getId());
            debug.put("isActive", userDetails.getUser().getIsActive());
        }

        return ResponseEntity.ok(debug);
    }

    @GetMapping("/ceo-test")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<Map<String, Object>> testCeoAccess(Authentication authentication) {
        logger.info("=== CEO TEST ENDPOINT CALLED ===");

        Map<String, Object> result = new HashMap<>();
        result.put("message", "CEO access successful");
        result.put("user", authentication.getName());

        return ResponseEntity.ok(result);
    }
}
