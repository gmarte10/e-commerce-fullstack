package com.giancarlos.controller;

import com.giancarlos.dto.*;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import com.giancarlos.model.User;
import com.giancarlos.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.ZonedDateTime;
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
    private final CartItemService cartItemService;

    public OrderController(OrderItemService orderItemService, OrderService orderService, ProductService productService, UserService userService, CartItemService cartItemService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.cartItemService = cartItemService;
    }

    @GetMapping("/user/{email}")
    public ResponseEntity<List<OrderDto>> getAllOrdersByUserEmail(@PathVariable String email) {
        Long userId = userService.getUserByEmail(email).getId();
        List<OrderDto> orderDtos = orderService.findByUserId(userId);
        return new ResponseEntity<>(orderDtos, HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<OrderResponseDto> addOrder(@RequestBody OrderRequestDto orderRequestDto) {
        String email = orderRequestDto.getEmail();
        UserDto user = userService.getUserByEmail(email);
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        OrderDto orderDto = new OrderDto();
        orderDto.setCreatedAt(ZonedDateTime.now());
        orderDto.setUserId(user.getId());
        orderDto.setTotalAmount(orderRequestDto.getTotal());
        OrderDto savedOrder = orderService.createOrder(orderDto);
        for (Long cartItemId : orderRequestDto.getCartItemIds()) {
            CartItemDto cartItemDto = cartItemService.findById(cartItemId);
            ProductDto product = productService.findById(cartItemDto.getProductId());
            OrderItemDto orderItemDto = new OrderItemDto();
            orderItemDto.setOrderId(savedOrder.getId());
            orderItemDto.setPrice(product.getPrice());
            orderItemDto.setQuantity(cartItemDto.getQuantity());
            orderItemDto.setProductId(product.getId());
            OrderItemDto saved = orderItemService.addOrderItem(orderItemDto);
            orderItemDtos.add(saved);
        }
        OrderResponseDto orderResponseDto = new OrderResponseDto();
        List<Long> oItemIds = new ArrayList<>();
        for (OrderItemDto o : orderItemDtos) {
            oItemIds.add(o.getId());
        }
        orderResponseDto.setOrderItemIds(oItemIds);
        orderResponseDto.setOrderId(orderDto.getId());

        return new ResponseEntity<>(orderResponseDto, HttpStatus.CREATED);
    }
}
