package com.giancarlos.dto.orderItem;

import com.giancarlos.dto.product.ProductResponseDto;
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
    private String imgBase64;
    private Integer quantity;
}
