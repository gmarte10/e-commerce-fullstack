package com.giancarlos.controller;

import com.giancarlos.dto.*;
import com.giancarlos.model.OrderItem;
import com.giancarlos.service.OrderItemService;
import com.giancarlos.service.OrderService;
import com.giancarlos.service.ProductService;
import com.giancarlos.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final UserService userService;

    public OrderItemController(OrderItemService orderItemService, OrderService orderService, ProductService productService, UserService userService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDto> orderItemDtos = orderItemService.getOrderItemsByOrderId(orderId);
        return new ResponseEntity<>(orderItemDtos, HttpStatus.OK);
    }
}
