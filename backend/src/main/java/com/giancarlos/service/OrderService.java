package com.giancarlos.service;

import com.giancarlos.dto.OrderDto;
import com.giancarlos.dto.OrderItemDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.repository.OrderRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OrderService {
    private OrderRepository orderRepository;
    private CartItemService cartItemService;
    public OrderService(OrderRepository orderRepository, CartItemService cartItemService) {
        this.orderRepository = orderRepository;
        this.cartItemService = cartItemService;
    }

    public List<OrderDto> findByUserId(Long userId) {
        List<Order> orders = orderRepository.findByUserIdOrderByCreatedAtDesc(userId);
        List<OrderDto> orderDtos = new ArrayList<>();
        for (Order o : orders) {
            Long orderId = o.getId();
            Long uId = o.getUser().getId();
            OrderDto orderDto = new OrderDto();
            orderDto.setId(orderId);
            orderDto.setUserId(uId);
            orderDto.setCreatedAt(o.getCreatedAt());
            orderDto.setTotalAmount(o.getTotalAmount());
            orderDtos.add(orderDto);
        }
        return orderDtos;
    }

    public OrderDto findOrderById(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order was not found");
        }
        OrderDto orderDto = new OrderDto();
        Order o = order.get();
        orderDto.setTotalAmount(o.getTotalAmount());
        orderDto.setId(o.getId());
        orderDto.setUserId(o.getUser().getId());
        orderDto.setCreatedAt(o.getCreatedAt());
        return orderDto;
    }
    public Order findOrderById2(Long orderId) {
        Optional<Order> order = orderRepository.findById(orderId);
        if (order.isEmpty()) {
            throw new OrderNotFoundException("Order was not found");
        }
        return order.get();
    }

}
