package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.ProductDTO;
import com.example.caliberclothing.dto.WishListDTO;
import com.example.caliberclothing.entity.Customer;
import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.entity.WishList;
import com.example.caliberclothing.repository.CustomerRepository;
import com.example.caliberclothing.repository.ProductRepository;
import com.example.caliberclothing.repository.WishListRepository;
import com.example.caliberclothing.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishlistRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public WishListDTO addToWishlist(Integer customerId, Integer productId) {
        // Check if already in wishlist
        if (wishlistRepository.existsByCustomerIdAndProductId(customerId, productId)) {
            throw new RuntimeException("Product already in wishlist");
        }

        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        WishList wishList = WishList.builder()
                .customer(customer)
                .product(product)
                .build();

        wishList = wishlistRepository.save(wishList);
        return convertToDTO(wishList);
    }

    @Override
    public void removeFromWishlist(Integer customerId, Integer productId) {
        wishlistRepository.deleteByCustomerIdAndProductId(customerId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<WishListDTO> getWishlistItems(Integer customerId) {
        return wishlistRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductInWishlist(Integer customerId, Integer productId) {
        return wishlistRepository.existsByCustomerIdAndProductId(customerId, productId);
    }

    @Override
    @Transactional(readOnly = true)
    public long getWishlistCount(Integer customerId) {
        return wishlistRepository.countByCustomerId(customerId);
    }

    private WishListDTO convertToDTO(WishList wishlist) {
        return WishListDTO.builder()
                .id(wishlist.getId())
                .customerId(wishlist.getCustomer().getId())
                .product(convertProductToDTO(wishlist.getProduct()))
                .createdTimestamp(wishlist.getCreatedTimestamp())
                .build();
    }

    private ProductDTO convertProductToDTO(Product product) {
        return ProductDTO.builder()
                .id(product.getId())
                .productNo(product.getProductNo())
                .name(product.getName())
                .description(product.getDescription())
                .productImage(product.getProductImage())
                .sellingPrice(product.getSellingPrice())
                .quantityInStock(product.getQuantityInStock())
                .build();
    }
}