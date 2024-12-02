package com.giancarlos.dto.orderItem;

import com.giancarlos.dto.order.OrderDto;
import com.giancarlos.dto.product.ProductDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OrderItemDto {
    private Long id;
    private OrderDto order;
    private ProductDto product;
    private Integer quantity;
    private BigDecimal price;
}
