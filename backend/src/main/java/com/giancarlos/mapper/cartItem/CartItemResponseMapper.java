package com.giancarlos.mapper.cartItem;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.dto.cartItem.CartItemResponseDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.mapper.product.ProductResponseMapper;
import org.springframework.stereotype.Component;

@Component
public class CartItemResponseMapper {
    private final ProductResponseMapper productResponseMapper;

    public CartItemResponseMapper(ProductResponseMapper productResponseMapper) {
        this.productResponseMapper = productResponseMapper;
    }

    public CartItemResponseDto toResponse(CartItemDto cartItemDto) {
        if (cartItemDto == null) {
            throw new CartItemNotFoundException("CartItemDto parameter is null in CartItemResponseMapper in toResponse");
        }
        return CartItemResponseDto.builder()
                .id(cartItemDto.getId())
                .quantity(cartItemDto.getQuantity())
                .product(productResponseMapper.toResponse(cartItemDto.getProduct()))
                .build();
    }
}
