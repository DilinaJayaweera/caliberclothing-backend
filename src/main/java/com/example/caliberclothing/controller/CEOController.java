package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.*;
import com.example.caliberclothing.entity.DeliveryServiceProvider;
import com.example.caliberclothing.entity.Product;
import com.example.caliberclothing.entity.SupplierDetails;
import com.example.caliberclothing.service.CustomerService;
import com.example.caliberclothing.service.DeliveryServiceProviderService;
import com.example.caliberclothing.service.EmployeeService;
import com.example.caliberclothing.service.OrderService;
import com.example.caliberclothing.service.ProductService;
import com.example.caliberclothing.service.SupplierDetailsService;
import com.example.caliberclothing.service.SupplierPaymentService;
import com.example.caliberclothing.service.impl.CustomUserDetails;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/ceo")
@PreAuthorize("hasRole('CEO')")
public class CEOController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private ProductService productService;

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DeliveryServiceProviderService deliveryServiceProviderService;

    @Autowired
    private SupplierDetailsService supplierDetailsService;

    @Autowired
    private SupplierPaymentService supplierPaymentService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getCEODashboard(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to CEO Dashboard");
        response.put("username", userDetails.getUsername());
        response.put("role", userDetails.getRole());
        response.put("dashboardType", "ceo");
        response.put("permissions", new String[]{
                "employee_management", "product_management", "order_viewing",
                "customer_viewing", "delivery_management", "supplier_management",
                "reports_generation"
        });

        return ResponseEntity.ok(response);
    }

    // Employee Management
    @PostMapping("/employees")
    public ResponseEntity<EmployeeDTO> createEmployee(
            @Valid @RequestBody CreateEmployeeRequest request,
            Authentication authentication) {

        try {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            int ceoId = userDetails.getUser().getId();

            // Convert request to DTOs
            EmployeeDTO employeeDTO = EmployeeDTO.builder()
                    .employeeNo(request.getEmployeeNo())
                    .fullName(request.getFullName())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .dateOfBirth(request.getDateOfBirth())
                    .sex(request.getSex())
                    .civilStatus(request.getCivilStatus())
                    .address(request.getAddress())
                    .mobileNumber(request.getMobileNumber())
                    .telephoneNumber(request.getTelephoneNumber())
                    .nicNo(request.getNicNo())
                    .build();

            UserDTO userDTO = UserDTO.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .isActive(true)
                    .build();

            StatusDTO statusDTO = StatusDTO.builder()
                    .id(request.getStatusId())
                    .build();

            EmployeeDTO createdEmployee = employeeService.createEmployee(employeeDTO, userDTO, request.getRole(), statusDTO, ceoId);
            return ResponseEntity.ok(createdEmployee);

        } catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> updateEmployee(
            @PathVariable int id,
            @Valid @RequestBody EmployeeUpdateRequest employeeData,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int ceoId = userDetails.getUser().getId();

        EmployeeDTO updatedEmployee = employeeService.updateEmployee(id, employeeData, ceoId);
        return ResponseEntity.ok(updatedEmployee);
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(
            @PathVariable int id,
            Authentication authentication) {

        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        int ceoId = userDetails.getUser().getId();

        employeeService.deleteEmployee(id, ceoId);
        return ResponseEntity.noContent().build();
    }

    private static final Logger logger = LoggerFactory.getLogger(CEOController.class);

    @GetMapping("/employees")
    public ResponseEntity<List<EmployeeDTO>> getAllEmployees(Authentication authentication) {
        logger.info("=== CEO EMPLOYEES ENDPOINT CALLED ===");
        logger.info("Authentication object: {}", authentication);
        logger.info("Principal: {}", authentication.getPrincipal());
        logger.info("Authorities: {}", authentication.getAuthorities());

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
            logger.info("User ID: {}", userDetails.getUser().getId());
            logger.info("Username: {}", userDetails.getUsername());
            logger.info("Role: {}", userDetails.getRole());
            logger.info("Is Active: {}", userDetails.getUser().getIsActive());
        }

        List<EmployeeDTO> employees = employeeService.getAllEmployees();
        logger.info("Found {} employees", employees.size());
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<EmployeeDTO> getEmployeeById(@PathVariable int id) {
        EmployeeDTO employee = employeeService.getEmployeeById(id);
        return ResponseEntity.ok(employee);
    }

    // Product Management
    @GetMapping("/products")
    public ResponseEntity<List<Product>> getAllProducts() {
        List<Product> products = productService.getAllActiveProducts();
        return ResponseEntity.ok(products);
    }

    @PostMapping("/products")
    public ResponseEntity<Product> createProduct(@Valid @RequestBody Product product) {
        Product savedProduct = productService.saveProduct(product);
        return ResponseEntity.ok(savedProduct);
    }

    @PutMapping("/products/{id}")
    public ResponseEntity<Product> updateProduct(@PathVariable Integer id, @Valid @RequestBody Product product) {
        Optional<Product> existingProduct = productService.getProductById(id);
        if (existingProduct.isPresent()) {
            product.setId(id);
            // Preserve the original createdTimestamp from existing product
            product.setCreatedTimestamp(existingProduct.get().getCreatedTimestamp());

            Product updatedProduct = productService.updateProduct(product);
            return ResponseEntity.ok(updatedProduct);
        }
        return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        productService.softDeleteProduct(id);
        return ResponseEntity.noContent().build();
    }

    // Order Viewing (Read-only)
    @GetMapping("/orders")
    public ResponseEntity<?> getAllOrders() {
        return ResponseEntity.ok(orderService.getAllOrders());
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Integer id) {
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    // Customer Viewing (without credentials)
    @GetMapping("/customers")
    public ResponseEntity<?> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<?> getCustomerById(@PathVariable int id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    // Delivery Service Provider Management
    @GetMapping("/delivery-providers")
    public ResponseEntity<List<DeliveryServiceProvider>> getAllDeliveryProviders() {
        return ResponseEntity.ok(deliveryServiceProviderService.getAllProviders());
    }

    @PostMapping("/delivery-providers")
    public ResponseEntity<DeliveryServiceProvider> createDeliveryProvider(@Valid @RequestBody DeliveryServiceProvider provider) {
        return ResponseEntity.ok(deliveryServiceProviderService.createProvider(provider));
    }

    @PutMapping("/delivery-providers/{id}")
    public ResponseEntity<DeliveryServiceProvider> updateDeliveryProvider(@PathVariable int id, @Valid @RequestBody DeliveryServiceProvider provider) {
        return ResponseEntity.ok(deliveryServiceProviderService.updateProvider(id, provider));
    }

    @DeleteMapping("/delivery-providers/{id}")
    public ResponseEntity<Void> deleteDeliveryProvider(@PathVariable int id) {
        deliveryServiceProviderService.deleteProvider(id);
        return ResponseEntity.noContent().build();
    }

    // Supplier Management
    @GetMapping("/suppliers")
    public ResponseEntity<List<SupplierDetails>> getAllSuppliers() {
        return ResponseEntity.ok(supplierDetailsService.getAllActiveSuppliers());
    }

    @PostMapping("/suppliers")
    public ResponseEntity<SupplierDetails> createSupplier(@Valid @RequestBody SupplierDetails supplier) {
        return ResponseEntity.ok(supplierDetailsService.saveSupplier(supplier));
    }

    @PutMapping("/suppliers/{id}")
    public ResponseEntity<SupplierDetails> updateSupplier(@PathVariable Integer id, @Valid @RequestBody SupplierDetails supplier) {
        supplier.setId(id);
        return ResponseEntity.ok(supplierDetailsService.updateSupplier(supplier));
    }

    @DeleteMapping("/suppliers/{id}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable Integer id) {
        supplierDetailsService.softDeleteSupplier(id);
        return ResponseEntity.noContent().build();
    }

    // Helper methods to extract DTOs from request
    private EmployeeDTO extractEmployeeDTO(Map<String, Object> data) {
        // Implementation to extract EmployeeDTO from Map
        return EmployeeDTO.builder().build(); // Placeholder
    }

    private UserDTO extractUserDTO(Map<String, Object> data) {
        // Implementation to extract UserDTO from Map
        return UserDTO.builder().build(); // Placeholder
    }

    private StatusDTO extractStatusDTO(Map<String, Object> data) {
        // Implementation to extract StatusDTO from Map
        return StatusDTO.builder().build(); // Placeholder
    }
}