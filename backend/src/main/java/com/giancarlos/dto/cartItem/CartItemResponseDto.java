package com.giancarlos.dto.cartItem;

import com.giancarlos.dto.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponseDto {
    private Long id;
    private ProductResponseDto product;
    private Integer quantity;
}
