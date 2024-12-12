package com.giancarlos.dto.order;

import com.giancarlos.dto.orderItem.OrderItemResponseDto;
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
public class OrderResponseDto {
    private Long id;
    private BigDecimal totalAmount;
    private ZonedDateTime createdAt;
    private List<Long> orderItemIds;
}
