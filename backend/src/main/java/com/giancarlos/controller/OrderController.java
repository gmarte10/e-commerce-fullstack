package com.giancarlos.controller;

import com.giancarlos.dto.OrderDto;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import com.giancarlos.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final UserService userService;

    public OrderController(OrderItemService orderItemService, OrderService orderService, ProductService productService, UserService userService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserEmail(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        List<OrderDto> orderDtos = orderService.findByUserId(userId);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }
}
