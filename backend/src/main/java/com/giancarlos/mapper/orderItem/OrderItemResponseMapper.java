package com.giancarlos.mapper.orderItem;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemResponseDto;
import com.giancarlos.exception.OrderItemNotFoundException;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.mapper.product.ProductMapper;
import com.giancarlos.mapper.product.ProductResponseMapper;
import com.giancarlos.repository.OrderItemRepository;
import org.springframework.stereotype.Component;

@Component
public class OrderItemResponseMapper {
    private final ProductResponseMapper productResponseMapper;
    public OrderItemResponseMapper(ProductResponseMapper productResponseMapper) {
        this.productResponseMapper = productResponseMapper;

    }
    public OrderItemResponseDto toResponse(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            throw new OrderItemNotFoundException("OrderItemDto is null in OrderItemResponseMapper in toResponse");
        }
        return OrderItemResponseDto.builder()
                .id(orderItemDto.getId())
                .price(orderItemDto.getPrice())
                .quantity(orderItemDto.getQuantity())
                .orderId(orderItemDto.getOrder().getId())
                .product(productResponseMapper.toResponse(orderItemDto.getProduct()))
                .build();
    }
}
