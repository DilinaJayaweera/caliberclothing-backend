package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.ChangePasswordRequest;
import com.example.caliberclothing.dto.LoginRequest;
import com.example.caliberclothing.dto.LoginResponse;
import com.example.caliberclothing.dto.RegisterRequest;
import com.example.caliberclothing.entity.User;
import com.example.caliberclothing.service.impl.CustomUserDetails;
import com.example.caliberclothing.service.impl.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserService userServiceImpl;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword()
                    )
            );

            SecurityContextHolder.getContext().setAuthentication(authentication);

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            String role = userDetails.getRole();
            String redirectUrl = getRedirectUrl(role);

            LoginResponse response = new LoginResponse(
                    true,
                    "Login successful",
                    role,
                    redirectUrl,
                    userDetails.getUsername(),
                    null // Add JWT token here if implementing JWT
            );

            return ResponseEntity.ok(response);

        } catch (BadCredentialsException e) {
            return ResponseEntity.badRequest().body(new LoginResponse(
                    false,
                    "Invalid username or password",
                    null,
                    null,
                    null,
                    null
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(new LoginResponse(
                    false,
                    "Authentication failed: " + e.getMessage(),
                    null,
                    null,
                    null,
                    null
            ));
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        try {
            if (userServiceImpl.existsByUsername(registerRequest.getUsername())) {
                Map<String, Object> response = new HashMap<>();
                response.put("success", false);
                response.put("message", "Username already exists");
                return ResponseEntity.badRequest().body(response);
            }

            User user = User.builder()
                    .username(registerRequest.getUsername())
                    .password(registerRequest.getPassword())
                    .role(registerRequest.getRole())
                    .isActive(true)
                    .build();

            userServiceImpl.createUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "User registered successfully");
            response.put("username", user.getUsername());
            response.put("role", user.getRole());

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("success", false);
            response.put("message", "Registration failed: " + e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            new SecurityContextLogoutHandler().logout(request, response, auth);
        }

        Map<String, Object> responseBody = new HashMap<>();
        responseBody.put("success", true);
        responseBody.put("message", "Logout successful");

        return ResponseEntity.ok(responseBody);
    }

    @GetMapping("/user")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            Map<String, Object> response = new HashMap<>();
            response.put("username", userDetails.getUsername());
            response.put("role", userDetails.getRole());
            response.put("isActive", userDetails.getUser().getIsActive());

            return ResponseEntity.ok(response);
        }

        return ResponseEntity.badRequest().body(Map.of("message", "User not authenticated"));
    }

    private String getRedirectUrl(String role) {
        return switch (role.toUpperCase()) {
            case "CEO" -> "/ceo/dashboard";
            case "PRODUCT_MANAGER" -> "/product_manager/dashboard";
            case "MERCHANDISE_MANAGER" -> "/merchandise_manager/dashboard";
            case "DISPATCH_OFFICER" -> "/dispatch_officer/dashboard";
            case "CUSTOMER" -> "/products";
            default -> "/dashboard";
        };
    }

    @PostMapping("/change-password")
    public ResponseEntity<?> changePassword(@Valid @RequestBody ChangePasswordRequest changePasswordRequest,
                                            Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "User not authenticated"));
            }

            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            // Verify current password
            if (!passwordEncoder.matches(changePasswordRequest.getCurrentPassword(), userDetails.getPassword())) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Current password is incorrect"));
            }

            // Verify new password and confirm password match
            if (!changePasswordRequest.getNewPassword().equals(changePasswordRequest.getConfirmPassword())) {
                return ResponseEntity.badRequest().body(Map.of("success", false, "message", "New password and confirm password do not match"));
            }

            // Update password
            User user = userDetails.getUser();
            user.setPassword(passwordEncoder.encode(changePasswordRequest.getNewPassword()));
            userServiceImpl.updateUser(user);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Password changed successfully");

            return ResponseEntity.ok(response);

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", "Password change failed: " + e.getMessage()));
        }
    }
}