package com.giancarlos.mapper.orderItem;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemRequestDto;
import com.giancarlos.exception.OrderItemNotFoundException;
import com.giancarlos.service.OrderService;
import com.giancarlos.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class OrderItemRequestMapper {
    private final OrderService orderService;
    private final ProductService productService;
    public OrderItemRequestMapper(OrderService orderService, ProductService productService) {
        this.orderService = orderService;
        this.productService = productService;

    }
    public OrderItemDto toDto(OrderItemRequestDto orderItemRequestDto) {
        if (orderItemRequestDto == null) {
            throw new OrderItemNotFoundException("OrderItemRequestDto is null in OrderItemRequestMapper in toDto");
        }
        return OrderItemDto.builder()
                .quantity(orderItemRequestDto.getQuantity())
                .price(orderItemRequestDto.getPrice())
                .order(orderService.getOrderById(orderItemRequestDto.getOrderId()))
                .product(productService.getProductById(orderItemRequestDto.getProductId()))
                .build();
    }
}
