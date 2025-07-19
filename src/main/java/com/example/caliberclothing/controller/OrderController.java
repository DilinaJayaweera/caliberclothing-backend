package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.OrderDTO;
import com.example.caliberclothing.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
//@CrossOrigin(origins = "*")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<OrderDTO> createOrder(@Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO createdOrder = orderService.createOrder(orderDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrder);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderDTO> updateOrder(@PathVariable Integer id,
                                                @Valid @RequestBody OrderDTO orderDTO) {
        OrderDTO updatedOrder = orderService.updateOrder(id, orderDTO);
        return ResponseEntity.ok(updatedOrder);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderDTO> getOrderById(@PathVariable Integer id) {
        OrderDTO order = orderService.getOrderById(id);
        return ResponseEntity.ok(order);
    }

    @GetMapping("/order-no/{orderNo}")
    public ResponseEntity<OrderDTO> getOrderByOrderNo(@PathVariable String orderNo) {
        OrderDTO order = orderService.getOrderByOrderNo(orderNo);
        return ResponseEntity.ok(order);
    }

    @GetMapping
    public ResponseEntity<List<OrderDTO>> getAllOrders() {
        List<OrderDTO> orders = orderService.getAllOrders();
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/customer/{customerId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByCustomerId(@PathVariable Integer customerId) {
        List<OrderDTO> orders = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(orders);
    }

//    @GetMapping("/employee/{employeeId}")
//    public ResponseEntity<List<OrderDTO>> getOrdersByEmployeeId(@PathVariable Integer employeeId) {
//        List<OrderDTO> orders = orderService.getOrdersByEmployeeId(employeeId);
//        return ResponseEntity.ok(orders);
//    }

    @GetMapping("/status/{statusId}")
    public ResponseEntity<List<OrderDTO>> getOrdersByStatusId(@PathVariable Integer statusId) {
        List<OrderDTO> orders = orderService.getOrdersByStatusId(statusId);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/date-range")
    public ResponseEntity<List<OrderDTO>> getOrdersByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        List<OrderDTO> orders = orderService.getOrdersByDateRange(startDate, endDate);
        return ResponseEntity.ok(orders);
    }

    @GetMapping("/price-range")
    public ResponseEntity<List<OrderDTO>> getOrdersByPriceRange(
            @RequestParam BigDecimal minPrice,
            @RequestParam BigDecimal maxPrice) {
        List<OrderDTO> orders = orderService.getOrdersByPriceRange(minPrice, maxPrice);
        return ResponseEntity.ok(orders);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Integer id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{orderNo}")
    public ResponseEntity<Boolean> existsByOrderNo(@PathVariable String orderNo) {
        boolean exists = orderService.existsByOrderNo(orderNo);
        return ResponseEntity.ok(exists);
    }

//    @PutMapping("/{id}/status")
//    @PreAuthorize("hasRole('DISPATCH_OFFICER') or hasRole('CEO')")
//    public ResponseEntity<OrderDTO> updateOrderStatus(@PathVariable Integer id,
//                                                      @RequestBody Map<String, Integer> statusUpdate) {
//        try {
//            OrderDTO updatedOrder = orderService.updateOrderStatus(id, statusUpdate.get("statusId"));
//            return ResponseEntity.ok(updatedOrder);
//        } catch (Exception e) {
//            return ResponseEntity.badRequest().build();
//        }
//    }
}
