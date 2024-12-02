package com.giancarlos.mapper.order;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.order.OrderRequestDto;
import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.user.UserDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.User;
import com.giancarlos.service.OrderItemService;
import com.giancarlos.service.UserService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Component
public class OrderRequestMapper {
    private final UserService userService;
    private final OrderItemService orderItemService;
    public OrderRequestMapper(UserService userService, OrderItemService orderItemService) {
        this.orderItemService = orderItemService;
        this.userService = userService;

    }
    public OrderDto toDto(OrderRequestDto orderRequestDto) {
        if (orderRequestDto == null) {
            throw new OrderNotFoundException("OrderRequestDto is null in OrderRequestMapper in toDto");
        }
        List<OrderItemDto> orderItems = getOrderItems(orderRequestDto.getProductIds());
        BigDecimal totalAmount = getTotalAmount(orderItems);
        ZonedDateTime createdAt = ZonedDateTime.now();

        return OrderDto.builder()
                .user(userService.getUserByEmail(orderRequestDto.getEmail()))
                .orderItems(orderItems)
                .totalAmount(totalAmount)
                .createdAt(createdAt)
                .build();
    }

    private List<OrderItemDto> getOrderItems(List<Long> orderItemIds) {
        List<OrderItemDto> orderItemDtos = new ArrayList<>();
        for (Long id : orderItemIds) {
            orderItemDtos.add(orderItemService.getOrderItemById(id));
        }
        return orderItemDtos;
    }

    private BigDecimal getTotalAmount(List<OrderItemDto> orderItemDtos) {
        BigDecimal total = BigDecimal.valueOf(0);
        for (OrderItemDto orderItemDto : orderItemDtos) {
            total = total.add(orderItemDto.getPrice());
        }
        return total;
    }
}
