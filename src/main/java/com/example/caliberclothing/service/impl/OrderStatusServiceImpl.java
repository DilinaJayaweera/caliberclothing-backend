package com.example.caliberclothing.service.impl;

import com.example.caliberclothing.dto.OrderStatusDTO;
import com.example.caliberclothing.entity.OrderStatus;
import com.example.caliberclothing.repository.OrderStatusRepository;
import com.example.caliberclothing.service.OrderStatusService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    @Override
    public OrderStatusDTO createOrderStatus(OrderStatusDTO orderStatusDTO) {
        if (existsByValue(orderStatusDTO.getValue())) {
            throw new IllegalArgumentException("Order status value already exists");
        }

        OrderStatus orderStatus = OrderStatus.builder()
                .value(orderStatusDTO.getValue())
                .build();

        OrderStatus savedOrderStatus = orderStatusRepository.save(orderStatus);
        return convertToDTO(savedOrderStatus);
    }

    @Override
    public OrderStatusDTO updateOrderStatus(Integer id, OrderStatusDTO orderStatusCreateDTO) {
        OrderStatus orderStatus = orderStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order status not found with id: " + id));

        if (!orderStatus.getValue().equals(orderStatusCreateDTO.getValue()) &&
                existsByValue(orderStatusCreateDTO.getValue())) {
            throw new IllegalArgumentException("Order status value already exists");
        }

        orderStatus.setValue(orderStatusCreateDTO.getValue());

        OrderStatus updatedOrderStatus = orderStatusRepository.save(orderStatus);
        return convertToDTO(updatedOrderStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatusDTO getOrderStatusById(Integer id) {
        OrderStatus orderStatus = orderStatusRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Order status not found with id: " + id));
        return convertToDTO(orderStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderStatusDTO getOrderStatusByValue(String value) {
        OrderStatus orderStatus = orderStatusRepository.findByValue(value)
                .orElseThrow(() -> new EntityNotFoundException("Order status not found with value: " + value));
        return convertToDTO(orderStatus);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderStatusDTO> getAllOrderStatuses() {
        return orderStatusRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteOrderStatus(Integer id) {
        if (!orderStatusRepository.existsById(id)) {
            throw new EntityNotFoundException("Order status not found with id: " + id);
        }
        orderStatusRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean existsByValue(String value) {
        return orderStatusRepository.existsByValue(value);
    }

    private OrderStatusDTO convertToDTO(OrderStatus orderStatus) {
        return OrderStatusDTO.builder()
                .id(orderStatus.getId())
                .value(orderStatus.getValue())
                .build();
    }
}
