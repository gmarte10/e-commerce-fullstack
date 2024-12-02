package com.giancarlos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderItemDisplayDto {
    private Long orderItemId;
    private String name;
    private BigDecimal price;
    private String category;
    private String imageBase64;
    private Integer quantity;
    private Long orderId;
}
