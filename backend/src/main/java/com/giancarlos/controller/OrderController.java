package com.giancarlos.controller;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderRequestDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.order.OrderRequestMapper;
import com.giancarlos.mapper.order.OrderResponseMapper;
import com.giancarlos.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final OrderResponseMapper orderResponseMapper;
    private final OrderRequestMapper orderRequestMapper;

    public OrderController(OrderRequestMapper orderRequestMapper,
                           OrderResponseMapper orderResponseMapper,
                           OrderService orderService,
                           UserService userService) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderResponseMapper = orderResponseMapper;
        this.orderRequestMapper = orderRequestMapper;
    }

    @GetMapping("/get/{email}")
    public ResponseEntity<List<OrderResponseDto>> getOrdersByUserEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        List<OrderDto> orderDtos = orderService.getOrdersByUserId(user.getId());
        List<OrderResponseDto> response = new ArrayList<>();
        for (OrderDto orderDto : orderDtos) {
            response.add(orderResponseMapper.toResponse(orderDto));
        }
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping("/create")
    public ResponseEntity<OrderResponseDto> createOrder(@RequestBody OrderRequestDto orderRequestDto) {
        OrderDto saved = orderService.createOrder(orderRequestMapper.toDto(orderRequestDto));
        OrderResponseDto response = orderResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
