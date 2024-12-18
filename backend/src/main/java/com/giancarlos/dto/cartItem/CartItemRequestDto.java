package com.giancarlos.dto.cartItem;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemRequestDto {
    private String email;
    private Long productId;
    private Integer quantity;
}