package com.giancarlos.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemAsProductDto {
    private Long cartItemId;
    private Long productId;
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String imageBase64;
    private Integer quantity;
}
