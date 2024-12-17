package com.giancarlos.mapper.orderItem;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.exception.OrderNotFoundException;
import com.giancarlos.model.OrderItem;
import com.giancarlos.model.Product;
import org.springframework.stereotype.Component;

@Component
public class OrderItemMapper {
    public OrderItemDto toDto(OrderItem orderItem) {
        if (orderItem == null) {
            throw new OrderNotFoundException("Order parameter is null in OrderMapper toDto");
        }
        if (orderItem.getProduct() == null) {
            return OrderItemDto.builder()
                    .id(orderItem.getId())
                    .orderId(orderItem.getOrder().getId())
                    .price(orderItem.getPrice())
                    .productId(-1L)
                    .quantity(orderItem.getQuantity())
                    .build();

        }
        return OrderItemDto.builder()
                .id(orderItem.getId())
                .orderId(orderItem.getOrder().getId())
                .price(orderItem.getPrice())
                .productId(orderItem.getProduct().getId())
                .quantity(orderItem.getQuantity())
                .build();
    }
}
