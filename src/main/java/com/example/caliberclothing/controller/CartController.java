package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.CartDTO;
import com.example.caliberclothing.service.CartService;
import com.example.caliberclothing.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
@PreAuthorize("hasRole('CUSTOMER')")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@RequestBody Map<String, Object> request,
                                       Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer customerId = userDetails.getUser().getId(); // You'll need to get customer ID from user
            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");

            CartDTO cartItem = cartService.addToCart(customerId, productId, quantity);
            return ResponseEntity.ok(Map.of("success", true, "data", cartItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateCartItem(@RequestBody Map<String, Object> request,
                                            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer customerId = userDetails.getUser().getId();
            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");

            CartDTO cartItem = cartService.updateCartItem(customerId, productId, quantity);
            return ResponseEntity.ok(Map.of("success", true, "data", cartItem));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/remove/{productId}")
    public ResponseEntity<?> removeFromCart(@PathVariable Integer productId,
                                            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer customerId = userDetails.getUser().getId();

            cartService.removeFromCart(customerId, productId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Item removed from cart"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/clear")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer customerId = userDetails.getUser().getId();

            cartService.clearCart(customerId);
            return ResponseEntity.ok(Map.of("success", true, "message", "Cart cleared"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping
    public ResponseEntity<?> getCartItems(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer customerId = userDetails.getUser().getId();

            List<CartDTO> cartItems = cartService.getCartItems(customerId);
            return ResponseEntity.ok(Map.of("success", true, "data", cartItems));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/count")
    public ResponseEntity<?> getCartItemCount(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer customerId = userDetails.getUser().getId();

            Integer count = cartService.getTotalItemsInCart(customerId);
            return ResponseEntity.ok(Map.of("success", true, "count", count));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}