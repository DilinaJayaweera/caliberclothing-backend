package com.example.caliberclothing.controller;

import com.example.caliberclothing.dto.OrderStatusDTO;
import com.example.caliberclothing.service.OrderStatusService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-statuses")
//@CrossOrigin(origins = "*")
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    public OrderStatusController(OrderStatusService orderStatusService) {
        this.orderStatusService = orderStatusService;
    }

    @PostMapping
    public ResponseEntity<OrderStatusDTO> createOrderStatus(@Valid @RequestBody OrderStatusDTO orderStatusDTO) {
        OrderStatusDTO createdOrderStatus = orderStatusService.createOrderStatus(orderStatusDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdOrderStatus);
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatusDTO> updateOrderStatus(@PathVariable Integer id,
                                                            @Valid @RequestBody OrderStatusDTO orderStatusDTO) {
        OrderStatusDTO updatedOrderStatus = orderStatusService.updateOrderStatus(id, orderStatusDTO);
        return ResponseEntity.ok(updatedOrderStatus);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatusDTO> getOrderStatusById(@PathVariable Integer id) {
        OrderStatusDTO orderStatus = orderStatusService.getOrderStatusById(id);
        return ResponseEntity.ok(orderStatus);
    }

    @GetMapping("/value/{value}")
    public ResponseEntity<OrderStatusDTO> getOrderStatusByValue(@PathVariable String value) {
        OrderStatusDTO orderStatus = orderStatusService.getOrderStatusByValue(value);
        return ResponseEntity.ok(orderStatus);
    }

    @GetMapping
    public ResponseEntity<List<OrderStatusDTO>> getAllOrderStatuses() {
        List<OrderStatusDTO> orderStatuses = orderStatusService.getAllOrderStatuses();
        return ResponseEntity.ok(orderStatuses);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable Integer id) {
        orderStatusService.deleteOrderStatus(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/exists/{value}")
    public ResponseEntity<Boolean> existsByValue(@PathVariable String value) {
        boolean exists = orderStatusService.existsByValue(value);
        return ResponseEntity.ok(exists);
    }
}
