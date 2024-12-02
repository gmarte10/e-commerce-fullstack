package com.giancarlos.dto.order;

import com.giancarlos.dto.orderItem.OrderItemDto;
import com.giancarlos.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderDto {
    private Long id;
    private UserDto user;
    private BigDecimal totalAmount;
    private ZonedDateTime createdAt;
    private List<OrderItemDto> orderItems;
}
