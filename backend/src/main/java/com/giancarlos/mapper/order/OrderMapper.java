package com.giancarlos.mapper.order;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.exception.UserNotFoundException;
import com.giancarlos.mapper.orderItem.OrderItemMapper;
import com.giancarlos.mapper.user.UserMapper;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    private final UserMapper userMapper;
    private final OrderItemMapper orderItemMapper;

    public OrderMapper(UserMapper userMapper, OrderItemMapper orderItemMapper) {
        this.userMapper = userMapper;
        this.orderItemMapper = orderItemMapper;
    }

    public OrderDto toDto(Order order) {
        if (order == null) {
            throw new OrderNotFoundException("Order parameter is null in OrderMapper toDto");
        }
        return OrderDto.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .user(userMapper.toDto(order.getUser()))
                .orderItems(mapOrderItemsToDtos(order.getOrderItems()))
                .build();
    }

    public Order toEntity(OrderDto orderDto) {
        if (orderDto == null) {
            throw new UserNotFoundException("OrderDto parameter is null in OrderMapper toEntity");
        }
        return Order.builder()
                .id(orderDto.getId())
                .createdAt(orderDto.getCreatedAt())
                .totalAmount(orderDto.getTotalAmount())
                .user(userMapper.toEntity(orderDto.getUser()))
                .orderItems(mapDtosToOrderItems(orderDto.getOrderItems()))
                .build();
    }

    private List<OrderItemDto> mapOrderItemsToDtos(List<OrderItem> orderItems) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (OrderItem orderItem : orderItems) {
            orderItemDtos.add(orderItemMapper.toDto(orderItem));
        }
        return orderItemDtos;
    }

    private List<OrderItem> mapDtosToOrderItems(List<OrderItemDto> orderItemDtos) {
        List<OrderItem> orderItems = new ArrayList<>();
        for (OrderItemDto orderItemDto : orderItemDtos) {
            orderItems.add(orderItemMapper.toEntity(orderItemDto));
        }
        return orderItems;
    }
}
