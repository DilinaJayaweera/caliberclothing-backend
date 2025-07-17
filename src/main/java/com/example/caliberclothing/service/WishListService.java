package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.WishListDTO;

import java.util.List;

public interface WishListService {
    WishListDTO addToWishlist(Integer customerId, Integer productId);
    void removeFromWishlist(Integer customerId, Integer productId);
    List<WishListDTO> getWishlistItems(Integer customerId);
    boolean isProductInWishlist(Integer customerId, Integer productId);
    long getWishlistCount(Integer customerId);
}