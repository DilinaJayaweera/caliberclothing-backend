package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.*;
import com.example.caliberclothing.entity.Customer;
import com.example.caliberclothing.service.*;
import com.example.caliberclothing.service.impl.CustomUserDetails;
import com.example.caliberclothing.repository.CustomerRepository;
import com.example.caliberclothing.repository.UserRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private CartService cartService;

    @Autowired
    private WishListService wishListService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private ProductService productService;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private UserRepository userRepository;

    // =========================== DASHBOARD & PROFILE ===========================

    @GetMapping("/dashboard")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCustomerDashboard(Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            Integer customerId = getCustomerIdFromAuth(authentication);

            // Get dashboard statistics
            Integer cartItemCount = cartService.getTotalItemsInCart(customerId);
            long wishlistCount = wishListService.getWishlistCount(customerId);
            List<OrderDTO> recentOrders = orderService.getOrdersByCustomerId(customerId)
                    .stream().limit(5).toList();

            Map<String, Object> response = new HashMap<>();
            response.put("message", "Welcome to Customer Dashboard");
            response.put("username", userDetails.getUsername());
            response.put("role", userDetails.getRole());
            response.put("dashboardType", "customer");
            response.put("permissions", new String[]{
                    "view_products", "manage_cart", "manage_wishlist",
                    "place_orders", "view_order_history", "view_receipts"
            });
            response.put("statistics", Map.of(
                    "cartItems", cartItemCount != null ? cartItemCount : 0,
                    "wishlistItems", wishlistCount,
                    "totalOrders", orderService.getOrdersByCustomerId(customerId).size(),
                    "recentOrders", recentOrders
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCustomerProfile(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            CustomerDTO customer = customerService.getCustomerById(customerId);

            // Remove sensitive information
            CustomerDTO sanitizedCustomer = CustomerDTO.builder()
                    .id(customer.getId())
                    .fullName(customer.getFullName())
                    .firstName(customer.getFirstName())
                    .lastName(customer.getLastName())
                    .dateOfBirth(customer.getDateOfBirth())
                    .sex(customer.getSex())
                    .email(customer.getEmail())
                    .address(customer.getAddress())
                    .country(customer.getCountry())
                    .zipCode(customer.getZipCode())
                    .mobileNumber(customer.getMobileNumber())
                    .createdTimestamp(customer.getCreatedTimestamp())
                    .updatedTimestamp(customer.getUpdatedTimestamp())
                    .province(customer.getProvince())
                    .build();

            return ResponseEntity.ok(Map.of("success", true, "data", sanitizedCustomer));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/profile")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateCustomerProfile(
            @Valid @RequestBody CustomerDTO customerDTO,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            // Ensure customer can only update their own profile
            customerDTO.setId(customerId);

            CustomerDTO updatedCustomer = customerService.updateCustomer(customerId, customerDTO);

            // Remove sensitive information
            CustomerDTO sanitizedCustomer = CustomerDTO.builder()
                    .id(updatedCustomer.getId())
                    .fullName(updatedCustomer.getFullName())
                    .firstName(updatedCustomer.getFirstName())
                    .lastName(updatedCustomer.getLastName())
                    .dateOfBirth(updatedCustomer.getDateOfBirth())
                    .sex(updatedCustomer.getSex())
                    .email(updatedCustomer.getEmail())
                    .address(updatedCustomer.getAddress())
                    .country(updatedCustomer.getCountry())
                    .zipCode(updatedCustomer.getZipCode())
                    .mobileNumber(updatedCustomer.getMobileNumber())
                    .updatedTimestamp(updatedCustomer.getUpdatedTimestamp())
                    .province(updatedCustomer.getProvince())
                    .build();

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Profile updated successfully",
                    "data", sanitizedCustomer
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // =========================== CART MANAGEMENT ===========================

    @GetMapping("/cart")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCartItems(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            List<CartDTO> cartItems = cartService.getCartItems(customerId);

            // Calculate cart totals
            BigDecimal subtotal = cartItems.stream()
                    .map(CartDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int totalItems = cartItems.stream()
                    .mapToInt(CartDTO::getQuantity)
                    .sum();

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("items", cartItems);
            response.put("summary", Map.of(
                    "subtotal", subtotal,
                    "totalItems", totalItems,
                    "itemCount", cartItems.size()
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/cart/add")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addToCart(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");

            // Validate input
            if (productId == null || quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid product ID or quantity"
                ));
            }

            // Check if product exists and is active
            Optional<com.example.caliberclothing.entity.Product> product = productService.getActiveProductById(productId);
            if (product.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Product not found or inactive"
                ));
            }

            // Check stock availability
            if (product.get().getQuantityInStock() < quantity) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Insufficient stock. Available: " + product.get().getQuantityInStock()
                ));
            }

            CartDTO cartItem = cartService.addToCart(customerId, productId, quantity);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Item added to cart successfully",
                    "data", cartItem
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PutMapping("/cart/update")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> updateCartItem(
            @RequestBody Map<String, Object> request,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            Integer productId = (Integer) request.get("productId");
            Integer quantity = (Integer) request.get("quantity");

            // Validate input
            if (productId == null || quantity == null || quantity <= 0) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Invalid product ID or quantity"
                ));
            }

            // Check if item exists in cart
            if (!cartService.isProductInCart(customerId, productId)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Item not found in cart"
                ));
            }

            // Check stock availability
            Optional<com.example.caliberclothing.entity.Product> product = productService.getActiveProductById(productId);
            if (product.isPresent() && product.get().getQuantityInStock() < quantity) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Insufficient stock. Available: " + product.get().getQuantityInStock()
                ));
            }

            CartDTO cartItem = cartService.updateCartItem(customerId, productId, quantity);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cart item updated successfully",
                    "data", cartItem
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/cart/remove/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> removeFromCart(
            @PathVariable Integer productId,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            // Check if item exists in cart
            if (!cartService.isProductInCart(customerId, productId)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Item not found in cart"
                ));
            }

            cartService.removeFromCart(customerId, productId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Item removed from cart successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/cart/clear")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> clearCart(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            cartService.clearCart(customerId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Cart cleared successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/cart/count")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCartItemCount(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            Integer count = cartService.getTotalItemsInCart(customerId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", count != null ? count : 0
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/cart/total")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCartTotal(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            List<CartDTO> cartItems = cartService.getCartItems(customerId);

            BigDecimal total = cartItems.stream()
                    .map(CartDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "total", total,
                    "itemCount", cartItems.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // =========================== WISHLIST MANAGEMENT ===========================

    @GetMapping("/wishlist")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getWishlistItems(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            List<WishListDTO> wishlistItems = wishListService.getWishlistItems(customerId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "items", wishlistItems,
                    "count", wishlistItems.size()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/wishlist/add/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> addToWishlist(
            @PathVariable Integer productId,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            // Check if product exists and is active
            Optional<com.example.caliberclothing.entity.Product> product = productService.getActiveProductById(productId);
            if (product.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Product not found or inactive"
                ));
            }

            // Check if already in wishlist
            if (wishListService.isProductInWishlist(customerId, productId)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Product already in wishlist"
                ));
            }

            WishListDTO wishlistItem = wishListService.addToWishlist(customerId, productId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Item added to wishlist successfully",
                    "data", wishlistItem
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @DeleteMapping("/wishlist/remove/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> removeFromWishlist(
            @PathVariable Integer productId,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            // Check if item exists in wishlist
            if (!wishListService.isProductInWishlist(customerId, productId)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Item not found in wishlist"
                ));
            }

            wishListService.removeFromWishlist(customerId, productId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Item removed from wishlist successfully"
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/wishlist/count")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getWishlistCount(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            long count = wishListService.getWishlistCount(customerId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "count", count
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/wishlist/move-to-cart/{productId}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> moveWishlistItemToCart(
            @PathVariable Integer productId,
            @RequestBody(required = false) Map<String, Object> request,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            Integer quantity = 1; // Default quantity

            if (request != null && request.containsKey("quantity")) {
                quantity = (Integer) request.get("quantity");
            }

            // Check if item exists in wishlist
            if (!wishListService.isProductInWishlist(customerId, productId)) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Item not found in wishlist"
                ));
            }

            // Check stock availability
            Optional<com.example.caliberclothing.entity.Product> product = productService.getActiveProductById(productId);
            if (product.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Product not found or inactive"
                ));
            }

            if (product.get().getQuantityInStock() < quantity) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Insufficient stock. Available: " + product.get().getQuantityInStock()
                ));
            }

            // Add to cart
            CartDTO cartItem = cartService.addToCart(customerId, productId, quantity);

            // Remove from wishlist
            wishListService.removeFromWishlist(customerId, productId);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "message", "Item moved to cart successfully",
                    "cartItem", cartItem
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // =========================== ORDER MANAGEMENT ===========================

    @GetMapping("/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getOrderHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "orderDate") String sortBy,
            @RequestParam(defaultValue = "desc") String sortDir,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            List<OrderDTO> allOrders = orderService.getOrdersByCustomerId(customerId);

            // Sort orders
            if ("desc".equalsIgnoreCase(sortDir)) {
                allOrders.sort((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()));
            } else {
                allOrders.sort((a, b) -> a.getOrderDate().compareTo(b.getOrderDate()));
            }

            // Paginate manually (you could use Pageable if OrderService supports it)
            int start = page * size;
            int end = Math.min(start + size, allOrders.size());
            List<OrderDTO> pageOrders = allOrders.subList(start, end);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("orders", pageOrders);
            response.put("pagination", Map.of(
                    "currentPage", page,
                    "pageSize", size,
                    "totalElements", allOrders.size(),
                    "totalPages", (int) Math.ceil((double) allOrders.size() / size),
                    "hasNext", end < allOrders.size(),
                    "hasPrevious", page > 0
            ));

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/orders/{id}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getOrderDetails(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            OrderDTO order = orderService.getOrderById(id);

            // Verify this order belongs to the current customer
            if (!order.getCustomer().getId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Access denied"));
            }

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "order", order
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @PostMapping("/orders")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> placeOrder(
            @Valid @RequestBody OrderDTO orderDTO,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            // Validate cart is not empty
            List<CartDTO> cartItems = cartService.getCartItems(customerId);
            if (cartItems.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of(
                        "success", false,
                        "message", "Cart is empty"
                ));
            }

            // Validate stock availability for all cart items
            for (CartDTO cartItem : cartItems) {
                Optional<com.example.caliberclothing.entity.Product> product =
                        productService.getActiveProductById(cartItem.getProduct().getId());
                if (product.isEmpty()) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "message", "Product " + cartItem.getProduct().getName() + " is no longer available"
                    ));
                }
                if (product.get().getQuantityInStock() < cartItem.getQuantity()) {
                    return ResponseEntity.badRequest().body(Map.of(
                            "success", false,
                            "message", "Insufficient stock for " + cartItem.getProduct().getName() +
                                    ". Available: " + product.get().getQuantityInStock()
                    ));
                }
            }

            // Set customer ID from authenticated user
            if (orderDTO.getCustomer() == null) {
                orderDTO.setCustomer(CustomerDTO.builder().id(customerId).build());
            } else {
                orderDTO.getCustomer().setId(customerId);
            }

            // Set order date if not provided
            if (orderDTO.getOrderDate() == null) {
                orderDTO.setOrderDate(LocalDateTime.now());
            }

            // Generate order number if not provided
            if (orderDTO.getOrderNo() == null || orderDTO.getOrderNo().isEmpty()) {
                orderDTO.setOrderNo(generateOrderNumber());
            }

            OrderDTO createdOrder = orderService.createOrder(orderDTO);

            // Clear cart after successful order
            cartService.clearCart(customerId);

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Order placed successfully",
                    "order", createdOrder
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/orders/status/{orderNo}")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getOrderStatus(
            @PathVariable String orderNo,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            OrderDTO order = orderService.getOrderByOrderNo(orderNo);

            // Verify this order belongs to the current customer
            if (!order.getCustomer().getId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Access denied"));
            }

            Map<String, Object> statusInfo = new HashMap<>();
            statusInfo.put("orderNo", order.getOrderNo());
            statusInfo.put("status", order.getOrderStatus());
            statusInfo.put("orderDate", order.getOrderDate());
            statusInfo.put("totalPrice", order.getTotalPrice());
            statusInfo.put("shippingAddress", order.getShippingAddress());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "statusInfo", statusInfo
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/orders/{id}/receipt")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getOrderReceipt(
            @PathVariable Integer id,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);
            OrderDTO order = orderService.getOrderById(id);

            // Verify this order belongs to the current customer
            if (!order.getCustomer().getId().equals(customerId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body(Map.of("success", false, "message", "Access denied"));
            }

            // Create receipt data
            Map<String, Object> receipt = new HashMap<>();
            receipt.put("orderNo", order.getOrderNo());
            receipt.put("orderDate", order.getOrderDate());
            receipt.put("customer", Map.of(
                    "name", order.getCustomer().getFullName(),
                    "email", order.getCustomer().getEmail()
            ));
            receipt.put("items", List.of(Map.of(
                    "quantity", order.getQuantity(),
                    "unitPrice", order.getUnitPrice(),
                    "totalPrice", order.getTotalPrice()
            )));
            receipt.put("subtotal", order.getTotalPrice());
            receipt.put("total", order.getTotalPrice());
            receipt.put("paymentInfo", order.getPayment());
            receipt.put("shippingAddress", order.getShippingAddress());
            receipt.put("status", order.getOrderStatus());

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "receipt", receipt
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // =========================== PAYMENT INFORMATION ===========================

    @GetMapping("/payment-info")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getPaymentInfo() {
        try {
            Map<String, Object> paymentInfo = new HashMap<>();
            paymentInfo.put("bankName", "Caliber Bank");
            paymentInfo.put("accountNumber", "1234567890");
            paymentInfo.put("accountName", "Caliber Clothing Ltd");
            paymentInfo.put("branchCode", "001");
            paymentInfo.put("swiftCode", "CALILKLX");
            paymentInfo.put("instructions", List.of(
                    "Please include your order number in the payment reference",
                    "Upload payment receipt after making the payment",
                    "Orders will be processed after payment verification"
            ));

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "paymentInfo", paymentInfo
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    // =========================== CUSTOMER REGISTRATION (Public) ===========================

    @PostMapping("/register")
    public ResponseEntity<?> registerCustomer(
            @Valid @RequestBody Map<String, Object> registrationData) {
        try {
            // Extract customer and user data from the registration payload
            CustomerDTO customerDTO = extractCustomerFromRegistration(registrationData);
            UserDTO userDTO = extractUserFromRegistration(registrationData);

            CustomerDTO createdCustomer = customerService.createCustomer(customerDTO, userDTO);

            // Remove sensitive information from response
            CustomerDTO responseCustomer = CustomerDTO.builder()
                    .id(createdCustomer.getId())
                    .fullName(createdCustomer.getFullName())
                    .email(createdCustomer.getEmail())
                    .createdTimestamp(createdCustomer.getCreatedTimestamp())
                    .build();

            return ResponseEntity.status(HttpStatus.CREATED).body(Map.of(
                    "success", true,
                    "message", "Customer registered successfully",
                    "customer", responseCustomer
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of(
                    "success", false,
                    "message", "Registration failed: " + e.getMessage()
            ));
        }
    }

    // =========================== UTILITY METHODS ===========================

    private Integer getCustomerIdFromAuth(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getUser().getId();

        // Find customer by user ID
        Optional<Customer> customer = customerRepository.findAll().stream()
                .filter(c -> c.getUser().getId().equals(userId) && c.getDeletedTimestamp() == null)
                .findFirst();

        if (customer.isEmpty()) {
            throw new RuntimeException("Customer profile not found for user");
        }

        return customer.get().getId();
    }

    private String generateOrderNumber() {
        // Generate unique order number
        String timestamp = String.valueOf(System.currentTimeMillis());
        String orderNo = "ORD" + timestamp.substring(timestamp.length() - 8);

        // Ensure uniqueness
        while (orderService.existsByOrderNo(orderNo)) {
            orderNo = "ORD" + String.valueOf(System.currentTimeMillis()).substring(timestamp.length() - 8);
        }

        return orderNo;
    }

    private CustomerDTO extractCustomerFromRegistration(Map<String, Object> data) {
        return CustomerDTO.builder()
                .fullName((String) data.get("fullName"))
                .firstName((String) data.get("firstName"))
                .lastName((String) data.get("lastName"))
                .dateOfBirth(data.get("dateOfBirth") != null ?
                        java.time.LocalDate.parse((String) data.get("dateOfBirth")) : null)
                .sex((String) data.get("sex"))
                .email((String) data.get("email"))
                .address((String) data.get("address"))
                .country((String) data.get("country"))
                .zipCode((String) data.get("zipCode"))
                .mobileNumber((String) data.get("mobileNumber"))
                .nicNo((String) data.get("nicNo"))
                .status(StatusDTO.builder()
                        .id(data.get("statusId") != null ? (Integer) data.get("statusId") : 1)
                        .build())
                .province(ProvinceDTO.builder()
                        .id(data.get("provinceId") != null ? (Integer) data.get("provinceId") : 1)
                        .build())
                .build();
    }

    private UserDTO extractUserFromRegistration(Map<String, Object> data) {
        return UserDTO.builder()
                .username((String) data.get("username"))
                .password((String) data.get("password"))
                .role("CUSTOMER")
                .isActive(true)
                .build();
    }

    // =========================== ADMIN/EMPLOYEE ACCESS (for other roles) ===========================

    @PostMapping
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<CustomerDTO> createCustomer(
            @Valid @RequestBody CustomerDTO customerDTO,
            @RequestBody UserDTO userDTO,
            @RequestAttribute(required = false) Integer createdBy) {
        try {
            CustomerDTO createdCustomer = customerService.createCustomer(customerDTO, userDTO);
            return new ResponseEntity<>(createdCustomer, HttpStatus.CREATED);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable int id,
            @Valid @RequestBody CustomerDTO customerDTO,
            Authentication authentication) {
        try {
            // Check if customer is updating their own profile or if user has ceo/collector role
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            boolean isCustomerUpdatingOwn = false;

            if (userDetails.getRole().equals("CUSTOMER")) {
                Integer customerId = getCustomerIdFromAuth(authentication);
                isCustomerUpdatingOwn = customerId.equals(id);

                if (!isCustomerUpdatingOwn) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }

            CustomerDTO updatedCustomer = customerService.updateCustomer(id, customerDTO);
            return ResponseEntity.ok(updatedCustomer);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('CUSTOMER') or hasRole('CEO')")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable int id,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.getRole().equals("CUSTOMER")) {
                Integer customerId = getCustomerIdFromAuth(authentication);
                if (!customerId.equals(id)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }

            customerService.deleteCustomer(id);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR') or hasRole('CUSTOMER')")
    public ResponseEntity<CustomerDTO> getCustomerById(
            @PathVariable int id,
            Authentication authentication) {
        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            if (userDetails.getRole().equals("CUSTOMER")) {
                Integer customerId = getCustomerIdFromAuth(authentication);
                if (!customerId.equals(id)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
                }
            }

            CustomerDTO customer = customerService.getCustomerById(id);
            return ResponseEntity.ok(customer);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        try {
            List<CustomerDTO> customers = customerService.getAllCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/active")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        try {
            List<CustomerDTO> customers = customerService.getActiveCustomers();
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/status/{statusName}")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> getCustomersByStatus(@PathVariable String statusName) {
        try {
            List<CustomerDTO> customers = customerService.getCustomersByStatus(statusName);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/search")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String searchTerm) {
        try {
            List<CustomerDTO> customers = customerService.searchCustomers(searchTerm);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/created-between")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<List<CustomerDTO>> getCustomersCreatedBetween(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        try {
            List<CustomerDTO> customers = customerService.getCustomersCreatedBetween(startDate, endDate);
            return ResponseEntity.ok(customers);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<Long> getTotalCustomersCount() {
        try {
            long count = customerService.getTotalCustomersCount();
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/count/new")
    @PreAuthorize("hasRole('CEO')")
    public ResponseEntity<Long> getNewCustomersCount(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime since) {
        try {
            long count = customerService.getNewCustomersCount(since);
            return ResponseEntity.ok(count);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-email")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        try {
            boolean exists = customerService.isEmailExists(email);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/check-nic")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<Boolean> checkNicExists(@RequestParam String nicNo) {
        try {
            boolean exists = customerService.isNicExists(nicNo);
            return ResponseEntity.ok(exists);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/email/{email}")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<ApiResponse<CustomerDTO>> getCustomerByEmail(@PathVariable String email) {
        try {
            CustomerDTO customer = customerService.getCustomerByEmail(email);

            ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                    .success(true)
                    .message("Customer retrieved successfully")
                    .data(customer)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            ApiResponse<CustomerDTO> response = ApiResponse.<CustomerDTO>builder()
                    .success(false)
                    .message("Customer not found")
                    .data(null)
                    .build();
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/paginated")
    @PreAuthorize("hasRole('CEO') or hasRole('ORDER_COLLECTOR')")
    public ResponseEntity<ApiResponse<Page<CustomerDTO>>> getCustomersWithPagination(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        try {
            Sort sort = sortDir.equalsIgnoreCase("desc") ?
                    Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

            Pageable pageable = PageRequest.of(page, size, sort);
            Page<CustomerDTO> customers = customerService.getCustomersWithPagination(pageable);

            ApiResponse<Page<CustomerDTO>> response = ApiResponse.<Page<CustomerDTO>>builder()
                    .success(true)
                    .message("Customers retrieved successfully")
                    .data(customers)
                    .build();

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // =========================== ANALYTICS & INSIGHTS ===========================

    @GetMapping("/analytics/summary")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getCustomerAnalytics(Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            // Get customer order history
            List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);

            // Calculate analytics
            BigDecimal totalSpent = orders.stream()
                    .map(OrderDTO::getTotalPrice)
                    .reduce(BigDecimal.ZERO, BigDecimal::add);

            int totalOrders = orders.size();

            BigDecimal averageOrderValue = totalOrders > 0 ?
                    totalSpent.divide(BigDecimal.valueOf(totalOrders), 2, BigDecimal.ROUND_HALF_UP) :
                    BigDecimal.ZERO;

            // Get recent activity
            List<OrderDTO> recentOrders = orders.stream()
                    .sorted((a, b) -> b.getOrderDate().compareTo(a.getOrderDate()))
                    .limit(5)
                    .toList();

            // Get current cart and wishlist counts
            Integer cartCount = cartService.getTotalItemsInCart(customerId);
            long wishlistCount = wishListService.getWishlistCount(customerId);

            Map<String, Object> analytics = new HashMap<>();
            analytics.put("totalSpent", totalSpent);
            analytics.put("totalOrders", totalOrders);
            analytics.put("averageOrderValue", averageOrderValue);
            analytics.put("recentOrders", recentOrders);
            analytics.put("currentCartItems", cartCount != null ? cartCount : 0);
            analytics.put("wishlistItems", wishlistCount);

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "analytics", analytics
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/analytics/spending-trend")
    @PreAuthorize("hasRole('CUSTOMER')")
    public ResponseEntity<?> getSpendingTrend(
            @RequestParam(defaultValue = "6") int months,
            Authentication authentication) {
        try {
            Integer customerId = getCustomerIdFromAuth(authentication);

            LocalDateTime endDate = LocalDateTime.now();
            LocalDateTime startDate = endDate.minusMonths(months);

            List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId).stream()
                    .filter(order -> order.getOrderDate().isAfter(startDate))
                    .toList();

            // Group spending by month
            Map<String, BigDecimal> spendingByMonth = orders.stream()
                    .collect(Collectors.groupingBy(
                            order -> order.getOrderDate().getYear() + "-" +
                                    String.format("%02d", order.getOrderDate().getMonthValue()),
                            Collectors.mapping(OrderDTO::getTotalPrice,
                                    Collectors.reducing(BigDecimal.ZERO, BigDecimal::add))
                    ));

            return ResponseEntity.ok(Map.of(
                    "success", true,
                    "spendingTrend", spendingByMonth
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }
}