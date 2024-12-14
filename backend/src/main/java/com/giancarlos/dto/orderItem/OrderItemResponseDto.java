package com.giancarlos.dto.orderItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemResponseDto {
    private Long id;
    private Long orderId;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String imageBase64;
    private Integer quantity;
}
