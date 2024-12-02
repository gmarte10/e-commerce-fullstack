package com.giancarlos.mapper.order;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.orderItem.OrderItemResponseDto;
import com.giancarlos.dto.product.ProductResponseDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.mapper.orderItem.OrderItemResponseMapper;
import com.giancarlos.mapper.product.ProductResponseMapper;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderResponseMapper {
    private final OrderItemResponseMapper orderItemResponseMapper;

    public OrderResponseMapper(OrderItemResponseMapper orderItemResponseMapper) {
        this.orderItemResponseMapper = orderItemResponseMapper;
    }
    public OrderResponseDto toResponse(OrderDto orderDto) {
        if (orderDto == null) {
            throw new OrderNotFoundException("Order is null in OrderResponseMapper in toResponse");
        }
        return OrderResponseDto.builder()
                .id(orderDto.getId())
                .createdAt(orderDto.getCreatedAt())
                .totalAmount(orderDto.getTotalAmount())
                .orderItems(mapDtoToResponse(orderDto.getOrderItems()))
                .build();
    }
    private List<OrderItemResponseDto> mapDtoToResponse(List<OrderItemDto> orderItemDtos) {
        List<OrderItemResponseDto> orderItemResponseDtos = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            orderItemResponseDtos.add(orderItemResponseMapper.toResponse(orderItemDto));
        }
        return orderItemResponseDtos;
    }
}
