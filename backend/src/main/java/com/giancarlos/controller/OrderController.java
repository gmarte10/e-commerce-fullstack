package com.giancarlos.controller;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderRequestDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemRequestDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.mapper.order.OrderResponseMapper;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.User;
import com.giancarlos.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final UserService userService;
    private final OrderResponseMapper orderResponseMapper;
    private final OrderItemService orderItemService;
    private final ProductService productService;

    public OrderController(OrderResponseMapper orderResponseMapper,
                           OrderService orderService,
                           UserService userService,
                           OrderItemService orderItemService,
                           ProductService productService) {
        this.orderService = orderService;
        this.userService = userService;
        this.orderResponseMapper = orderResponseMapper;
        this.orderItemService = orderItemService;
        this.productService = productService;

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
        Long userId = userService.getUserByEmail(orderRequestDto.getEmail()).getId();
        User user = userService.getUserById(userId);
        List<OrderItem> orderItems = new ArrayList<>();
        Order order = Order.builder()
                .user(user)
                .createdAt(orderRequestDto.getCreatedAt())
                .totalAmount(orderRequestDto.getTotalAmount())
                .build();

        OrderDto saved = orderService.createOrder(order);
        for (OrderItemRequestDto requestDto : orderRequestDto.getOrderItemRequestDtos()) {
            OrderItem orderItem = OrderItem.builder()
                    .product(productService.getProductById(requestDto.getProductId()))
                    .quantity(requestDto.getQuantity())
                    .order(orderService.getOrderById(saved.getId()))
                    .price(requestDto.getPrice().multiply(BigDecimal.valueOf(requestDto.getQuantity())))
                    .build();
            OrderItemDto orderItemDto = orderItemService.createOrderItem(orderItem);
            orderItems.add(orderItemService.getOrderItemById(orderItemDto.getId()));
        }
        order = orderService.getOrderById(saved.getId());
        order.setOrderItems(orderItems);
        saved = orderService.createOrder(order);
        OrderResponseDto response = orderResponseMapper.toResponse(saved);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
