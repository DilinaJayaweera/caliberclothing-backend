package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.CustomerDTO;
import com.example.caliberclothing.dto.OrderDTO;
import com.example.caliberclothing.entity.DeliveryServiceProvider;
import com.example.caliberclothing.service.CustomerService;
import com.example.caliberclothing.service.DeliveryServiceProviderService;
import com.example.caliberclothing.service.OrderService;
import com.example.caliberclothing.service.OrderStatusService;
import com.example.caliberclothing.service.impl.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/dispatch-officer")
@PreAuthorize("hasRole('DISPATCH_OFFICER')")
public class DispatchOfficerController {

    @Autowired
    private OrderService orderService;

    @Autowired
    private CustomerService customerService;

    @Autowired
    private DeliveryServiceProviderService deliveryServiceProviderService;

    @Autowired
    private OrderStatusService orderStatusService;

    @GetMapping("/dashboard")
    public ResponseEntity<?> getDispatchOfficerDashboard(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Map<String, Object> response = new HashMap<>();
        response.put("message", "Welcome to Dispatch Officer Dashboard");
        response.put("username", userDetails.getUsername());
        response.put("role", userDetails.getRole());
        response.put("dashboardType", "dispatch_officer");
        response.put("permissions", new String[]{
                "order_viewing", "order_status_management", "customer_viewing",
                "delivery_provider_viewing"
        });

        return ResponseEntity.ok(response);
    }

    // Order Management
    @GetMapping("/orders")
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/orders/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomer(@PathVariable Integer customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/status/{statusId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatus(@PathVariable Integer statusId) {
        List<OrderDTO> orders = orderService.getOrdersByStatusId(statusId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/date-range")
    public ResponseEntity<List<OrderDTO>> getOrdersByDateRange(
            @RequestParam LocalDateTime startDate,
            @RequestParam LocalDateTime endDate) {
        List<OrderDTO> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/orders/price-range")
    public ResponseEntity<List<OrderDTO>> getOrdersByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<OrderDTO> orders = orderService.getOrdersByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(orders);
    }

    // Order Status Management
    @PutMapping("/orders/{id}/status")
    public ResponseEntity<?> updateOrderStatus(
            @PathVariable Integer id,
            @RequestBody Map<String, Integer> statusUpdate) {
        try {
            // Get the current order
            OrderDTO currentOrder = orderService.getOrderById(id);

            // Update only the status
            OrderDTO updateOrder = OrderDTO.builder()
                    .id(id)
                    .orderStatus(orderStatusService.getOrderStatusById(statusUpdate.get("statusId")))
                    .build();

            OrderDTO updatedOrder = orderService.updateOrder(id, updateOrder);

            Map<String, Object> response = new HashMap<>();
            response.put("success", true);
            response.put("message", "Order status updated successfully");
            response.put("order", updatedOrder);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("success", false, "message", e.getMessage()));
        }
    }

    @GetMapping("/order-statuses")
    public ResponseEntity<?> getAllOrderStatuses() {
        return ResponseEntity.ok(orderStatusService.getAllOrderStatuses());
    }

    // Customer Viewing
    @GetMapping("/customers")
    public ResponseEntity<List<CustomerDTO>> getAllCustomers() {
        List<CustomerDTO> customers = customerService.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/{id}")
    public ResponseEntity<CustomerDTO> getCustomerById(@PathVariable int id) {
        CustomerDTO customer = customerService.getCustomerById(id);
        return ResponseEntity.ok(customer);
    }

    @GetMapping("/customers/search")
    public ResponseEntity<List<CustomerDTO>> searchCustomers(@RequestParam String searchTerm) {
        List<CustomerDTO> customers = customerService.searchCustomers(searchTerm);
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/customers/active")
    public ResponseEntity<List<CustomerDTO>> getActiveCustomers() {
        List<CustomerDTO> customers = customerService.getActiveCustomers();
        return ResponseEntity.ok(customers);
    }

    // Delivery Service Provider Viewing
    @GetMapping("/delivery-providers")
    public ResponseEntity<List<DeliveryServiceProvider>> getAllDeliveryProviders() {
        List<DeliveryServiceProvider> providers = deliveryServiceProviderService.getAllProviders();
        return ResponseEntity.ok(providers);
    }

    @GetMapping("/delivery-providers/{id}")
    public ResponseEntity<?> getDeliveryProviderById(@PathVariable int id) {
        return deliveryServiceProviderService.getProviderById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/delivery-providers/active")
    public ResponseEntity<List<DeliveryServiceProvider>> getActiveDeliveryProviders() {
        List<DeliveryServiceProvider> providers = deliveryServiceProviderService.getActiveProviders();
        return ResponseEntity.ok(providers);
    }

    // Dashboard Statistics
    @GetMapping("/statistics")
    public ResponseEntity<Map<String, Object>> getDashboardStatistics() {
        Map<String, Object> stats = new HashMap<>();

        // Get total orders count
        List<OrderDTO> allOrders = orderService.getAllOrders();
        stats.put("totalOrders", allOrders.size());

        // Get orders by status counts
        // You can implement more detailed statistics here

        // Get total customers count
        long totalCustomers = customerService.getTotalCustomersCount();
        stats.put("totalCustomers", totalCustomers);

        // Get active delivery providers count
        List<DeliveryServiceProvider> activeProviders = deliveryServiceProviderService.getActiveProviders();
        stats.put("activeDeliveryProviders", activeProviders.size());

        return ResponseEntity.ok(stats);
    }
}