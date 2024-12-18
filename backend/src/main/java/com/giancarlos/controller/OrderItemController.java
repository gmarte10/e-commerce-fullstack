package com.giancarlos.controller;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemResponseDto;
import com.giancarlos.mapper.orderItem.OrderItemResponseMapper;
import com.giancarlos.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

/**
 * OrderItemController handles the REST API endpoints related to order item management.
 * It provides functionality for retrieving order items for a specific order.
 *
 * This controller interacts with the following services:
 * 1. OrderItemService: Manages the order item-related business logic and data persistence.
 * 2. OrderItemResponseMapper: Maps OrderItemDto objects to OrderItemResponseDto objects for API responses.
 *
 * The available endpoint is:
 * - `GET /api/order-items/get/{orderId}`: Fetches all order items for a specific order identified by its ID.
 *
 * @RestController
 * @RequestMapping("/api/order-items")
 */

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    private final OrderItemService orderItemService;
    private final OrderItemResponseMapper orderItemResponseMapper;

    public OrderItemController(OrderItemResponseMapper orderItemResponseMapper, OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
        this.orderItemResponseMapper = orderItemResponseMapper;
    }

    @GetMapping("/get/{orderId}")
    public ResponseEntity<List<OrderItemResponseDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDto> orderItemDtos = orderItemService.getOrderItemsByOrderId(orderId);
        List<OrderItemResponseDto> response = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            response.add(orderItemResponseMapper.toResponse(orderItemDto));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
