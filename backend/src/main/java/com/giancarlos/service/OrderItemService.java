package com.giancarlos.service;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.exception.OrderItemNotFoundException;
import com.giancarlos.mapper.orderItem.OrderItemMapper;
import com.giancarlos.model.*;
import com.giancarlos.repository.OrderItemRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderItemService provides functionality for managing order items within an order.
 * This service interacts with the `OrderItemRepository` to perform CRUD operations
 * and maps entities to Data Transfer Objects (DTOs) using the `OrderItemMapper`.
 * It includes methods for retrieving, creating, and handling order items.
 *
 * The service provides the following key functionalities:
 * 1. Retrieving an order item by its ID.
 * 2. Retrieving a list of order items associated with a specific order.
 * 3. Creating a new order item and saving it to the database.
 *
 * This service is annotated with @Service to be managed by the Spring container.
 *
 * @Service
 */

@Service
public class OrderItemService {
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;

    public OrderItemService(OrderItemRepository orderItemRepository,
                            OrderItemMapper orderItemMapper
                            ) {
        this.orderItemRepository = orderItemRepository;
        this.orderItemMapper = orderItemMapper;
    }

    public OrderItem getOrderItemById(Long orderItemId) {
        return orderItemRepository.findById(orderItemId).orElseThrow(() -> new OrderItemNotFoundException("Order Item not found"));
    }

    public List<OrderItemDto> getOrderItemsByOrderId(Long orderId) {
        List<OrderItem> orderItems = orderItemRepository.findByOrderId(orderId);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(orderItemMapper.toDto(orderItem));
        }
        return orderItemDtos;
    }

    public OrderItemDto createOrderItem(OrderItem orderItem) {
        OrderItem savedOrderItem = orderItemRepository.save(orderItem);
        return orderItemMapper.toDto(savedOrderItem);
    }
}
