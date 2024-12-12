package com.giancarlos.mapper.order;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.model.Order;
import com.giancarlos.model.OrderItem;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class OrderMapper {
    public OrderDto toDto(Order order) {
        if (order == null) {
            throw new OrderNotFoundException("Order parameter is null in OrderMapper toDto");
        }
        return OrderDto.builder()
                .id(order.getId())
                .createdAt(order.getCreatedAt())
                .totalAmount(order.getTotalAmount())
                .userId(order.getUser().getId())
                .orderItemIds(mapOrderItemsToIds(order.getOrderItems()))
                .build();
    }

    private List<Long> mapOrderItemsToIds(List<OrderItem> orderItems) {
        if (orderItems == null) {
            return null;
        }
        List<Long> ret = new ArrayList<>();
        for (OrderItem item : orderItems) {
            ret.add(item.getId());
        }
        return ret;
    }
}
