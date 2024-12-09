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
