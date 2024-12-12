package com.giancarlos.dto.cartItem;

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
public class CartItemResponseDto {
    private Long id;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String imgBase64;
    private Integer quantity;
}
