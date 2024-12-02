package com.giancarlos.controller;

import com.giancarlos.dto.*;
import com.giancarlos.model.OrderItem;
import com.giancarlos.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/order-items")
public class OrderItemController {
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final ProductService productService;
    private final UserService userService;
    private final ImageService imageService;

    public OrderItemController(OrderItemService orderItemService, OrderService orderService, ProductService productService, UserService userService, ImageService imageService) {
        this.orderItemService = orderItemService;
        this.orderService = orderService;
        this.productService = productService;
        this.userService = userService;
        this.imageService = imageService;
    }

    @GetMapping("/order/{id}")
    public ResponseEntity<List<OrderItemDto>> getOrderItemsByOrderId(@PathVariable Long orderId) {
        List<OrderItemDto> orderItemDtos = orderItemService.getOrderItemsByOrderId(orderId);
        return new ResponseEntity<>(orderItemDtos, HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<List<OrderItemDisplayDto>> getOrderItemsByUserEmail(@PathVariable String email) {
        UserDto user = userService.getUserByEmail(email);
        List<OrderDto> orders = orderService.findByUserId(user.getId());
        List<OrderItemDisplayDto> ret = new ArrayList<>();
        for (OrderDto o : orders) {
            List<OrderItemDto> items = orderItemService.getOrderItemsByOrderId(o.getId());
            for (OrderItemDto i : items) {
                OrderItemDisplayDto orderItemDisplayDto = new OrderItemDisplayDto();
                orderItemDisplayDto.setOrderId(o.getId());
                orderItemDisplayDto.setPrice(i.getPrice());
                orderItemDisplayDto.setQuantity(i.getQuantity());
                orderItemDisplayDto.setOrderItemId(i.getId());
                ProductDto product = productService.findById(i.getProductId());
                String base64 = imageService.getImageBase64(product.getImageURL());
                orderItemDisplayDto.setName(product.getName());
                orderItemDisplayDto.setCategory(product.getCategory());
                orderItemDisplayDto.setImageBase64(base64);
                ret.add(orderItemDisplayDto);
            }
        }
        return new ResponseEntity<>(ret, HttpStatus.OK);
    }
}
