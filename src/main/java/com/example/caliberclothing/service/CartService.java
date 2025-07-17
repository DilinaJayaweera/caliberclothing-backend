package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.CartDTO;
import java.util.List;

public interface CartService {
    CartDTO addToCart(Integer customerId, Integer productId, Integer quantity);
    CartDTO updateCartItem(Integer customerId, Integer productId, Integer quantity);
    void removeFromCart(Integer customerId, Integer productId);
    void clearCart(Integer customerId);
    List<CartDTO> getCartItems(Integer customerId);
    Integer getTotalItemsInCart(Integer customerId);
    boolean isProductInCart(Integer customerId, Integer productId);
}