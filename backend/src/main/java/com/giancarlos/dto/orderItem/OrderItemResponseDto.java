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
    private ProductResponseDto product;
    private Integer quantity;
    private BigDecimal price;
}
