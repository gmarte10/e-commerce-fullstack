package com.giancarlos.service;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.mapper.order.OrderMapper;
import com.giancarlos.model.Order;
import com.giancarlos.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    public OrderService(OrderRepository orderRepository, OrderMapper orderMapper) {
        this.orderRepository = orderRepository;
        this.orderMapper = orderMapper;
    }

    public List<OrderDto> getOrdersByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order order : orders) {
            orderDtos.add(orderMapper.toDto(order));
        }
        return orderDtos;
    }

    public Order getOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order was not found in OrderService getOrderById");
        }
        return order.get();
    }

    public OrderDto createOrder(Order order) {
        Order saved = orderRepository.save(order);
        return orderMapper.toDto(saved);
    }
}
