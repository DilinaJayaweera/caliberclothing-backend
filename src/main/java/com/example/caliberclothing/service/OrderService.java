package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.OrderDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {

    OrderDTO createOrder(OrderDTO orderDTO);

    OrderDTO updateOrder(Integer id, OrderDTO orderDTO);

    OrderDTO getOrderById(Integer id);

    OrderDTO getOrderByOrderNo(String orderNo);

    List<OrderDTO> getAllOrders();

    List<OrderDTO> getOrdersByCustomerId(Integer customerId);

//    List<OrderDTO> getOrdersByEmployeeId(Integer employeeId);

    List<OrderDTO> getOrdersByStatusId(Integer statusId);

    List<OrderDTO> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate);

    List<OrderDTO> getOrdersByPriceRange(BigDecimal minPrice, BigDecimal maxPrice);

    void deleteOrder(Integer id);

    boolean existsByOrderNo(String orderNo);
}

