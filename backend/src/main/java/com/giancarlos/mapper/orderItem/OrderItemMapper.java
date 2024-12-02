package com.giancarlos.mapper.orderItem;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.mapper.order.OrderMapper;
import com.giancarlos.model.OrderItem;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    private final ProductMapper productMapper;
    private final OrderMapper orderMapper;

    public OrderItemMapper(ProductMapper productMapper, OrderMapper orderMapper) {
        this.productMapper = productMapper;
        this.orderMapper = orderMapper;
    }

    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            throw new OrderNotFoundException("Order parameter is null in OrderMapper toDto");
        }
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .order(orderMapper.toDto(orderItem.getOrder()))
                .price(orderItem.getPrice())
                .product(productMapper.toDTO(orderItem.getProduct()))
                .quantity(orderItem.getQuantity())
                .build();
    }

    public OrderItem toEntity(OrderItemDto orderItemDto) {
        if (orderItemDto == null) {
            throw new UserNotFoundException("OrderDto parameter is null in OrderMapper toEntity");
        }
        return OrderItem.builder()
                .id(orderItemDto.getId())
                .order(orderMapper.toEntity(orderItemDto.getOrder()))
                .price(orderItemDto.getPrice())
                .product(productMapper.toEntity(orderItemDto.getProduct()))
                .quantity(orderItemDto.getQuantity())
                .build();
    }
}
