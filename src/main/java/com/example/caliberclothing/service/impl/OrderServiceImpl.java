package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.CustomerDTO;
import com.example.caliberclothing.dto.OrderDTO;
import com.example.caliberclothing.dto.OrderStatusDTO;
import com.example.caliberclothing.dto.PaymentDTO;
import com.example.caliberclothing.dto.DiscountDTO;
import com.example.caliberclothing.entity.*;
import com.example.caliberclothing.repository.EmployeeRepository;
import com.example.caliberclothing.repository.OrderRepository;
import com.example.caliberclothing.repository.OrderStatusRepository;
import com.example.caliberclothing.service.OrderService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final EmployeeRepository employeeRepository;

    @Override
    public OrderDTO createOrder(OrderDTO orderDTO) {
        if (existsByOrderNo(orderDTO.getOrderNo())) {
            throw new IllegalArgumentException("Order number already exists");
        }

        Employee employee = employeeRepository.findById(orderDTO.getEmployee().getId())
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        String employeeNo = employee.getEmployeeNo();

        Order order = Order.builder()
                .orderNo(orderDTO.getOrderNo())
                .quantity(orderDTO.getQuantity())
                .unitPrice(orderDTO.getUnitPrice())
                .totalPrice(orderDTO.getTotalPrice())
                .shippingAddress(orderDTO.getShippingAddress())
                .orderDate(orderDTO.getOrderDate())
                .customer(Customer.builder().id(orderDTO.getCustomer().getId()).build())
                .employee(employee)
                .orderStatus(OrderStatus.builder().id(orderDTO.getOrderStatus().getId()).build())
                .payment(Payment.builder().id(orderDTO.getPayment().getId()).build())
                .discount(orderDTO.getDiscount().getId() != null ?
                        Discount.builder().id(orderDTO.getDiscount().getId()).build() : null)
                .build();

        Order savedOrder = orderRepository.save(order);
        return convertToDTO(savedOrder);
    }

    @Override
    public OrderDTO updateOrder(Integer id, OrderDTO orderDTO) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));

        if (orderDTO.getQuantity() != null) {
            order.setQuantity(orderDTO.getQuantity());
        }
        if (orderDTO.getUnitPrice() != null) {
            order.setUnitPrice(orderDTO.getUnitPrice());
        }
        if (orderDTO.getTotalPrice() != null) {
            order.setTotalPrice(orderDTO.getTotalPrice());
        }
        if (orderDTO.getShippingAddress() != null) {
            order.setShippingAddress(orderDTO.getShippingAddress());
        }
        if (orderDTO.getOrderDate() != null) {
            order.setOrderDate(orderDTO.getOrderDate());
        }
        if (orderDTO.getCustomer() != null && orderDTO.getCustomer().getId() != null) {
            order.setCustomer(Customer.builder().id(orderDTO.getCustomer().getId()).build());
        }
        if (orderDTO.getEmployee() != null && orderDTO.getEmployee().getEmployeeNo() != null) {
            Employee employee = employeeRepository.findByEmployeeNo(orderDTO.getEmployee().getEmployeeNo())
                    .orElseThrow(() -> new EntityNotFoundException("Employee not found with employeeNo: " + orderDTO.getEmployee().getEmployeeNo()));
            order.setEmployee(employee);
        }
        if (orderDTO.getOrderStatus() != null && orderDTO.getOrderStatus().getId() != null) {
            order.setOrderStatus(OrderStatus.builder().id(orderDTO.getOrderStatus().getId()).build());
        }
        if (orderDTO.getPayment() != null && orderDTO.getPayment().getId() != null) {
            order.setPayment(Payment.builder().id(orderDTO.getPayment().getId()).build());
        }
        if (orderDTO.getDiscount() != null && orderDTO.getDiscount().getId() != null) {
            order.setDiscount(Discount.builder().id(orderDTO.getDiscount().getId()).build());
        }

        Order updatedOrder = orderRepository.save(order);
        return convertToDTO(updatedOrder);
    }


    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderById(Integer id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with id: " + id));
        return convertToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderDTO getOrderByOrderNo(String orderNo) {
        Order order = orderRepository.findByOrderNo(orderNo)
                .orElseThrow(() -> new EntityNotFoundException("Order not found with order number: " + orderNo));
        return convertToDTO(order);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getAllOrders() {
        return orderRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByCustomerId(Integer customerId) {
        return orderRepository.findByCustomerId(customerId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByEmployeeId(Integer employeeId) {
        return orderRepository.findByEmployeeId(employeeId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByStatusId(Integer statusId) {
        return orderRepository.findByOrderStatusId(statusId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return orderRepository.findByOrderDateBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderDTO> getOrdersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice) {
        return orderRepository.findByTotalPriceBetween(minPrice, maxPrice).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrder(Integer id) {
        if (!orderRepository.existsById(id)) {
            throw new EntityNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByOrderNo(String orderNo) {
        return orderRepository.existsByOrderNo(orderNo);
    }

    private OrderDTO convertToDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .orderNo(order.getOrderNo())
                .quantity(order.getQuantity())
                .unitPrice(order.getUnitPrice())
                .totalPrice(order.getTotalPrice())
                .shippingAddress(order.getShippingAddress())
                .orderDate(order.getOrderDate())
                .customer(order.getCustomer() != null ?
                        CustomerDTO.builder().id(order.getCustomer().getId()).build() : null)
                .employee(order.getEmployee() != null ?
                        Employee.builder().id(order.getEmployee().getId()).employeeNo(order.getEmployee().getEmployeeNo()).build() : null)
                .orderStatus(order.getOrderStatus() != null ?
                        OrderStatusDTO.builder().id(order.getOrderStatus().getId()).build() : null)
                .payment(order.getPayment() != null ?
                        PaymentDTO.builder().id(order.getPayment().getId()).build() : null)
                .discount(order.getDiscount() != null ?
                        DiscountDTO.builder().id(order.getDiscount().getId()).build() : null)
                .build();
    }

}