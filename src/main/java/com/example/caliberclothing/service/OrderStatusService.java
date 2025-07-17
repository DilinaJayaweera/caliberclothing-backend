package com.example.caliberclothing.service;

import com.example.caliberclothing.dto.OrderStatusDTO;

import java.util.List;

public interface OrderStatusService {

    OrderStatusDTO createOrderStatus(OrderStatusDTO orderStatusDTO);

    OrderStatusDTO updateOrderStatus(Integer id, OrderStatusDTO orderStatusDTO);

    OrderStatusDTO getOrderStatusById(Integer id);

    OrderStatusDTO getOrderStatusByValue(String value);

    List<OrderStatusDTO> getAllOrderStatuses();

    void deleteOrderStatus(Integer id);

    boolean existsByValue(String value);
}
