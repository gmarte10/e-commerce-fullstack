package com.giancarlos.service;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.mapper.orderItem.OrderItemMapper;
import com.giancarlos.model.*;
import com.giancarlos.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderItemMapper orderItemMapper) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    public OrderItemDto getOrderItemById(Long orderItemId) {
        return orderItemMapper.toDto(orderItemRepository.findById(orderItemId).orElseThrow());
    }

    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(orderItemMapper.toDto(orderItem));
        }
        return orderItemDtos;
    }

    public List<OrderItemDto> getOrderItemsByProductId(Long productId) {
        List<OrderItem> orderItems = orderItemRepository.findByProductId(productId);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(orderItemMapper.toDto(orderItem));
        }
        return orderItemDtos;
    }

    public OrderItemDto createOrderItem(OrderItemDto orderItemDto) {
        OrderItem savedOrderItem = orderItemRepository.save(orderItemMapper.toEntity(orderItemDto));
        return orderItemMapper.toDto(savedOrderItem);
    }
}
