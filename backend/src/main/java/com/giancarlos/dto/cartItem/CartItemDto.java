package com.giancarlos.dto.cartItem;

import com.giancarlos.dto.product.ProductDto;
import com.giancarlos.dto.user.UserDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CartItemDto {
    private Long id;
    private Long userId;
    private Long productId;
    private Integer quantity;
}
