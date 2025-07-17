package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.CartDTO;
import com.example.caliberclothing.dto.ProductDTO;
import com.example.caliberclothing.entity.Cart;
import com.example.caliberclothing.entity.Customer;
import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.repository.CartRepository;
import com.example.caliberclothing.repository.CustomerRepository;
import com.example.caliberclothing.repository.ProductRepository;
import com.example.caliberclothing.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Override
    public CartDTO addToCart(Integer customerId, Integer productId, Integer quantity) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new RuntimeException("Customer not found"));

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        // Check if item already exists in cart
        Optional<Cart> existingCartItem = cartRepository.findByCustomerIdAndProductId(customerId, productId);

        Cart cart;
        if (existingCartItem.isPresent()) {
            // Update quantity
            cart = existingCartItem.get();
            cart.setQuantity(cart.getQuantity() + quantity);
        } else {
            // Create new cart item
            cart = Cart.builder()
                    .customer(customer)
                    .product(product)
                    .quantity(quantity)
                    .build();
        }

        cart = cartRepository.save(cart);
        return convertToDTO(cart);
    }

    @Override
    public CartDTO updateCartItem(Integer customerId, Integer productId, Integer quantity) {
        Cart cart = cartRepository.findByCustomerIdAndProductId(customerId, productId)
                .orElseThrow(() -> new RuntimeException("Cart item not found"));

        cart.setQuantity(quantity);
        cart = cartRepository.save(cart);
        return convertToDTO(cart);
    }

    @Override
    public void removeFromCart(Integer customerId, Integer productId) {
        cartRepository.deleteByCustomerIdAndProductId(customerId, productId);
    }

    @Override
    public void clearCart(Integer customerId) {
        cartRepository.deleteByCustomerId(customerId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CartDTO> getCartItems(Integer customerId) {
        return cartRepository.findByCustomerId(customerId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Integer getTotalItemsInCart(Integer customerId) {
        Integer total = cartRepository.getTotalItemsInCart(customerId);
        return total != null ? total : 0;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isProductInCart(Integer customerId, Integer productId) {
        return cartRepository.existsByCustomerIdAndProductId(customerId, productId);
    }

    private CartDTO convertToDTO(Cart cart) {
        BigDecimal unitPrice = cart.getProduct().getSellingPrice();
        BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(cart.getQuantity()));

        return CartDTO.builder()
                .id(cart.getId())
                .customerId(cart.getCustomer().getId())
                .product(convertProductToDTO(cart.getProduct()))
                .quantity(cart.getQuantity())
                .unitPrice(unitPrice)
                .totalPrice(totalPrice)
                .createdTimestamp(cart.getCreatedTimestamp())
                .updatedTimestamp(cart.getUpdatedTimestamp())
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