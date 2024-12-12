package com.giancarlos.mapper.order;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderResponseDto;
import com.giancarlos.exception.OrderNotFoundException;
import org.springframework.stereotype.Component;

@Component
public class OrderResponseMapper {

    public OrderResponseDto toResponse(OrderDto orderDto) {
        if (orderDto == null) {
            throw new OrderNotFoundException("Order is null in OrderResponseMapper in toResponse");
        }
        return OrderResponseDto.builder()
                .id(orderDto.getId())
                .createdAt(orderDto.getCreatedAt())
                .totalAmount(orderDto.getTotalAmount())
                .orderItemIds(orderDto.getOrderItemIds())
                .build();
    }
}
