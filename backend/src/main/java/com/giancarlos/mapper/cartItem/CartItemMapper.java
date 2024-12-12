package com.giancarlos.mapper.cartItem;

import com.giancarlos.dto.cartItem.CartItemDto;
import com.giancarlos.exception.CartItemNotFoundException;
import com.giancarlos.model.CartItem;
import org.springframework.stereotype.Component;

@Component
public class CartItemMapper {
    public CartItemDto toDto(CartItem cartItem) {
        if (cartItem == null) {
            throw new CartItemNotFoundException("CartItem parameter is null in CartItemMapper toDto");
        }
        return CartItemDto.builder()
                .id(cartItem.getId())
                .userId(cartItem.getUser().getId())
                .productId(cartItem.getProduct().getId())
                .quantity(cartItem.getQuantity())
                .build();
    }
}
